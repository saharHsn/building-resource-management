package tech.builtrix.services.session;

import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import tech.builtrix.annotations.aspect.NoLog;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.exceptions.session.InternalServerException;
import tech.builtrix.exceptions.session.SessionDisabledException;
import tech.builtrix.exceptions.session.SessionExpiredException;
import tech.builtrix.models.session.Session;
import tech.builtrix.models.user.User;
import tech.builtrix.repositories.session.SessionRepository;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@ConfigurationProperties("builtrix.security")
public class SessionService extends GenericCrudServiceBase<Session, SessionRepository> {

    @Setter
    private int exp = 10000 * 24 * 3600;


    protected SessionService(SessionRepository sessionRepository) {
        super(sessionRepository);
    }

    @Cacheable(cacheNames = "SessionService.getSession", key = "#sessionKey")
    public Session getSession(String sessionKey) throws NotFoundException {
        Session session = this.repository.findBySessionKey(sessionKey);
        if (session == null) {
            throw new NotFoundException("Session", "sessionKey", sessionKey);
        }
        return session;
    }

    private Session createNewSession(User user) {
        Session session = new Session();
        session.setRequestCount(1);
        session.setLastRequestDate(new Date());
        setActive(session, true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.exp);
        session.setExpirationDate(calendar.getTime());
        session.setSessionKey(RandomStringUtils.random(36, true, true));
        session.setUser(user != null ? user.getId() : null);
        return this.repository.save(session);
    }

    Session create(@NotNull User user) throws InternalServerException {
        List<Session> sessions;
        try {
            if (user != null) {
                sessions = this.repository.findByUser(user.getId());
                Session session = processOldSession(sessions);
                if (session != null) return session;
            }
        } catch (Exception e) {
            throw new InternalServerException();
        }
        // this.repository.disableAllOtherSessions(user.getId());
        return createNewSession(user);
    }

    private Session processOldSession(List<Session> sessions) {
        Session session = sessions.size() > 0 ?
                sessions.get(0) : null;
        if (session != null && session.getExpirationDate().after(new Date())) {
            // touchSession(session);
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

    public String findUserSession(String userId) {
        List<Session> sessions = repository.findByUser(userId);
        Session session = processOldSession(sessions);
        return session.getSessionKey();

    }

    public Session findUserSessionToken(String sessionId) throws NotFoundException {
        Session session = repository.findBySessionKey(sessionId);
        //Session session = processOldSession(sessions);
        if (session == null) {
            throw new NotFoundException("Session", "sessionKey", sessionId);
        }
        return session;
    }

    public void updateSessionUserId(Session session) {
        repository.save(session);
    }

}
