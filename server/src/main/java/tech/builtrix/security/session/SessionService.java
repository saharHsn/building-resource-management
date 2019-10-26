package tech.builtrix.security.session;

import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.builtrix.aspect.NoLog;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exception.NotFoundException;
import tech.builtrix.model.user.User;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@ConfigurationProperties("investment.security")
public class SessionService extends GenericCrudServiceBase<Session, SessionRepository> {

    @Setter
    private int exp = 10000 * 24 * 3600;


    protected SessionService(SessionRepository sessionRepository) {
        super(sessionRepository);
    }


    @Cacheable(cacheNames = "SessionService.getSession", key = "#sessionKey")
    public Session getSession(String sessionKey) throws NotFoundException {
        Session session = this.repository.getBySessionKey(sessionKey);
        if (session == null) {
            throw new NotFoundException("Session", "sessionKey", sessionKey);
        }
        return session;
    }

    @NoLog
    public void getSessionIncrementRequestCount(Session session) {
        this.repository.incrementRequests(session.getSessionKey());
    }

    private Session createNewSession(@Nullable User user) {
        Session session = new Session();
        session.setRequestCount(1);
        session.setLastRequestDate(new Date());
        setActive(session, true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.exp);
        session.setExpirationDate(calendar.getTime());
        session.setSessionKey(RandomStringUtils.random(36, true, true));
        session.setUser(user);
        return this.repository.save(session);
    }


    void touchSession(Session session) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.exp);
        this.repository.touchSession(session.getId(), calendar.getTime());
    }

    public Session create(@NotNull User user) {
        Page<Session> sessions = this.repository.findUserSession(user.getId(),
                PageRequest.of(0, 1));
        Session session = processOldSession(sessions);
        if (session != null) return session;
        this.repository.disableAllOtherSessions(user.getId());
        return createNewSession(user);
    }

    private Session processOldSession(Page<Session> sessions) {
        Session session = sessions.getContent().size() > 0 ?
                sessions.getContent().get(0) : null;
        if (session != null && session.getExpirationDate().after(new Date())) {
            touchSession(session);
            return session;
        }
        return null;
    }

    @NoLog
    public void validate(Session session) throws SessionDisabledException, SessionExpiredException {
        if (!session.getActive()) {
            throw new SessionDisabledException();
        }
        if (session.getExpirationDate().before(new Date())) {
            throw new SessionExpiredException();
        }

    }

    public void expireSession(String sessionKey) throws NotFoundException {
        Session session = this.getSession(sessionKey);
        session.setExpirationDate(new Date());

        this.repository.save(session);
    }

    public String findUserSession(String deviceId, String userId) {
        Page<Session> sessions = repository.findUserSession(userId, PageRequest.of(0, 1));
        Session session = processOldSession(sessions);
        return session.getSessionKey();

    }

    public Session findUserSessionToken(String sessionId) throws NotFoundException {
        Page<Session> sessions = repository.findUserSessionToken(sessionId, PageRequest.of(0, 1));
        Session session = processOldSession(sessions);
        if (session == null) {
            throw new NotFoundException("Session", "sessionKey", sessionId);
        }
        return session;
    }

    public void updateSessionUserId(Session session) {
        repository.save(session);
    }

    public Session getTestSession(String mobileNumber) {
        List<Session> testSession = this.repository.getTestSession(mobileNumber);
        if (!CollectionUtils.isEmpty(testSession)) {
            return testSession.get(0);
        } else
            return null;
    }
}
