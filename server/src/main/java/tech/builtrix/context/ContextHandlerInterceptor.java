package tech.builtrix.context;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

@Component
@Slf4j
public class ContextHandlerInterceptor implements HandlerInterceptor {
    public static final String HEADER_X_PLATFORM = "X-Platform";
    public static final String HEADER_X_VERSION = "X-Version";
    private static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    private final RequestContext requestContext;

    @Autowired
    public ContextHandlerInterceptor(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object handler) throws Exception {
        RequestContextHolder.currentRequestAttributes().setAttribute("requestContext", this.requestContext, 0);
        // this is for ignoring accept-language
        LocaleContextHolder.setLocale(new Locale("fa", "IR"));
        HandlerMethod handlerMethod;
        Method method;
        String remoteAddr = "";
        if (httpServletRequest != null) {
            remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
            if (StringUtils.isEmpty(remoteAddr)) {
                remoteAddr = httpServletRequest.getRemoteAddr();
            }
        }
        requestContext.setClientIp(remoteAddr);
        logger.info(String.format("Request received [deviceId=%s,userId=%s,clientIp=%s]", MDC.get("deviceId"), MDC.get("userId"), MDC.get("clientIp")));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        MDC.clear();
    }
}




