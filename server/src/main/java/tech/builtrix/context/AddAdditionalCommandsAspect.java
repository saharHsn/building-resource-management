package tech.builtrix.context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.Response;
import tech.builtrix.commands.CommandBase;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

@Aspect
@Component
public class AddAdditionalCommandsAspect {

    private final RequestContext requestContext;

    public AddAdditionalCommandsAspect(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Around("target(tech.builtrix.base.ControllerBase+)")
    public Object addCommands(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (result instanceof Response && this.requestContext.getMustBeAddCommands().size() > 0) {
            for (CommandBase command : this.requestContext.getMustBeAddCommands()) {
                ((Response) result).addCommand(command);
            }
        }
        return result;
    }
}
