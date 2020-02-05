package tech.builtrix.services.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import tech.builtrix.exceptions.ExceptionBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.exceptions.session.SessionDisabledException;
import tech.builtrix.exceptions.session.SessionExpiredException;
import tech.builtrix.models.session.Session;
import tech.builtrix.models.user.User;

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

	private SessionService sessionService;

	@Autowired
	public SessionKeyService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	public String createToken(User user) throws ExceptionBase {
		Session session = this.sessionService.create(user);
		return session.getSessionKey();
	}

	public void expireToken(String sessionKey) throws NotFoundException {
		this.sessionService.expireSession(sessionKey);
	}

	public Session getAndValidateSession(String accessToken)
			throws NotFoundException, SessionDisabledException, SessionExpiredException {
		Session session = this.sessionService.getSession(accessToken);
		this.sessionService.validate(session);
		// this.sessionService.getSessionIncrementRequestCount(session);
		return session;
	}
}
