package tech.builtrix.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import tech.builtrix.exception.NotFoundException;
import tech.builtrix.model.user.User;
import tech.builtrix.security.session.Session;
import tech.builtrix.security.session.SessionDisabledException;
import tech.builtrix.security.session.SessionExpiredException;
import tech.builtrix.security.session.SessionService;

import javax.transaction.Transactional;

/**
 * @author : pc`
 * @date : 23/09/2019
 */

@Service
@EnableAutoConfiguration
@ConfigurationProperties
public class SessionKeyService {

    public static final String HeaderKey = "X-Session";
    public static final String CookieKey = "SESSION";
    public static final String QueryKey = "session";

    public static final String BuildNoKey = "X-BuildNo";
    public static final String VersionKey = "X-Version";
    public static final String OsVersionNoKey = "OS-Version";

    //private DeviceService deviceService;
    private SessionService sessionService;

    @Autowired
    public SessionKeyService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public String createToken(User user) {
        Session session = this.sessionService.create(user);
        return session.getSessionKey();
    }

    Session getTestSession(String mobileNumber) {
        return this.sessionService.getTestSession(mobileNumber);
    }


    void expireToken(String sessionKey) throws NotFoundException {
        this.sessionService.expireSession(sessionKey);
    }

    @Transactional
    public Session getAndValidateSession(String accessToken) throws NotFoundException, SessionDisabledException, SessionExpiredException {
        Session session = this.sessionService.getSession(accessToken);
        this.sessionService.validate(session);
        this.sessionService.getSessionIncrementRequestCount(session);
        return session;
    }
}
