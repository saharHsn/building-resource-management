package tech.builtrix.base;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tech.builtrix.common.service.LocalizationService;
import tech.builtrix.context.ContextHandlerInterceptor;
import tech.builtrix.context.RequestContext;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/
@ApiImplicitParams({
        @ApiImplicitParam(name = ContextHandlerInterceptor.HEADER_X_PLATFORM, paramType = "header", value = "Client Platform", allowableValues = "Ios, Android, Web"),
        @ApiImplicitParam(name = ContextHandlerInterceptor.HEADER_X_VERSION, paramType = "header", value = "Client Application Version")
})
public abstract class ControllerBase {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected RequestContext requestContext;
    @Autowired
    protected LocalizationService localizationService;

    protected String localizedText(String path, Object... params) {
        return this.localizationService.localizedText(path, params);
    }

    protected String localizedText(String path) {
        return this.localizationService.localizedText(path);
    }

    protected void logException(Exception ex) {
        logger.warn(ex.getMessage());
    }

}
