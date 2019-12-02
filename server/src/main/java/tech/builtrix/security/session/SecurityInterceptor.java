package tech.builtrix.security.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.builtrix.context.RequestContext;
import tech.builtrix.model.user.Role;
import tech.builtrix.security.PermissionDeniedException;
import tech.builtrix.security.SessionNotLoggedInException;
import tech.builtrix.security.annotations.Authorize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private RequestContext requestContext;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        HandlerMethod handlerMethod;
        Method method;
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
            method = handlerMethod.getMethod();
        } else {
            // todo wtf is wrong if no controller is assigned to handle this request
            return true;
        }
        if (!method.getDeclaringClass().getPackage().getName().contains("builtrix")) {
            return true;
        }
        if (method.isAnnotationPresent(NoSession.class) ||
                method.getDeclaringClass().isAnnotationPresent(NoSession.class)) {
            return true;
        }

        if (method.isAnnotationPresent(Authorize.class) ||
                method.getDeclaringClass().isAnnotationPresent(Authorize.class)) {

            // check if session is logged in session
            if (this.requestContext.getUser() == null) {
                throw new SessionNotLoggedInException();
            }

            Role[] requiredRoles = method.getAnnotation(Authorize.class) != null ?
                    method.getAnnotation(Authorize.class).roles() :
                    method.getDeclaringClass().getAnnotation(Authorize.class).roles();

            // if no role set User role is set
            if (requiredRoles == null || requiredRoles.length == 0) {
                requiredRoles = Role.values();
            }

            // if any rules matched
            for (Role role : requiredRoles) {
                if (this.requestContext.getUser().getRole() == role) {
                    return true;
                }
            }
            throw new PermissionDeniedException(requiredRoles);
        } else {
            // do not check permissions
            return true;
        }
    }


}


