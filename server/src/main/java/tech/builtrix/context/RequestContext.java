package tech.builtrix.context;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import tech.builtrix.commands.CommandBase;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
  * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class RequestContext {
    private HttpServletRequest request;
    private Long requestTime = Calendar.getInstance().getTimeInMillis();
    private List<CommandBase> mustBeAddCommands = new ArrayList<>();

    public RequestContext() {
    }

    List<CommandBase> getMustBeAddCommands() {
        return mustBeAddCommands;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    void setClientIp(String clientIp) {
    }

}
