package tech.builtrix.context;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import tech.builtrix.commands.CommandBase;
import tech.builtrix.exceptions.ExceptionBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.exceptions.session.DuplicatedHeaderException;
import tech.builtrix.exceptions.session.EmptySessionException;
import tech.builtrix.exceptions.session.InvalidSessionException;
import tech.builtrix.models.session.Session;
import tech.builtrix.models.user.User;
import tech.builtrix.services.session.SessionKeyService;
import tech.builtrix.services.user.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class RequestContext {

	private final SessionKeyService sessionKeyService;
	// private final MonitorService monitorService;

	private HttpServletRequest request;
	private String versionStr;
	private Long buildNo;
	private String requestUrl;
	private String requestHttpMethod;
	private String correlationId;
	private Long requestTime = Calendar.getInstance().getTimeInMillis();
	private String clientIp;
	private String sessionToken;
	private Session session;
	private List<CommandBase> mustBeAddCommands = new ArrayList<>();

	private UserService userService;

	@Autowired
	public RequestContext(SessionKeyService sessionKeyService, UserService userService) {
		this.userService = userService;
		this.correlationId = RandomStringUtils.random(10, true, true);
		this.sessionKeyService = sessionKeyService;
	}

	List<CommandBase> getMustBeAddCommands() {
		return mustBeAddCommands;
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

	void setRequest(HttpServletRequest request)
			throws InvalidSessionException, EmptySessionException, DuplicatedHeaderException {
		String sessionToken = getSessionFromQuery(request);
		if (StringUtils.isEmpty(sessionToken))
			sessionToken = getSessionFromHeader(request);
		if (StringUtils.isEmpty(sessionToken))
			sessionToken = getSessionFromCookies(request);
		if (StringUtils.isEmpty(sessionToken)) {
			// this.monitorService.addEvent(EventType.EPSE, "RequestContext.setRequest");
			throw new EmptySessionException();
		}
		this.request = request;
		try {
			this.session = this.sessionKeyService.getAndValidateSession(sessionToken);
		} catch (ExceptionBase ex) {
			throw new InvalidSessionException(ex);
		}
		this.sessionToken = sessionToken;
	}

	private String getSessionFromHeader(HttpServletRequest request) throws DuplicatedHeaderException {
		List<String> sessionValues = Collections.list(request.getHeaders(SessionKeyService.HeaderKey));
		if (sessionValues.size() == 0)
			return null;
		if (sessionValues.size() > 1) {
			// monitorService.addEvent(EventType.DHE,
			// "RequestContext.getSessionFromHeader");
			throw new DuplicatedHeaderException(SessionKeyService.HeaderKey);
		}
		return sessionValues.get(0);
	}

	private String getSessionFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase(SessionKeyService.CookieKey)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	private String getSessionFromQuery(HttpServletRequest request) {
		if (StringUtils.isEmpty(request.getQueryString())) {
			return null;
		}
		String[] queryParams = request.getQueryString().split("&");
		for (String query : queryParams) {
			if (query.startsWith("?"))
				query = query.substring(1);

			String[] parts = query.split("=");
			if (parts[0].equals(SessionKeyService.QueryKey)) {
				return parts[1];
			}
		}
		return null;
	}

	public String getSessionKey() {
		if (StringUtils.isEmpty(this.sessionToken))
			return null;
		return this.session != null ? this.session.getSessionKey() : null;
	}

	private User user;

	public User getUser() {
		logger.info("session : " + session);
		if (this.session == null) {
			return null;
		}
		logger.info("session.user : " + this.session.getUser());
		if (this.session.getUser() == null) {
			return null;
		}
		if (user == null) {
			try {
				user = this.userService.getById(this.session.getUser());
			} catch (NotFoundException e) {
				return null;
			}
		}
		return user;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public Long getRequestTime() {
		return requestTime;
	}

	String getClientIp() {
		return clientIp;
	}

	void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getVersionStr() {
		return versionStr;
	}

	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}

	public Long getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(Long buildNo) {
		this.buildNo = buildNo;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestHttpMethod() {
		return requestHttpMethod;
	}

	public void setRequestHttpMethod(String requestHttpMethod) {
		this.requestHttpMethod = requestHttpMethod;
	}
}
