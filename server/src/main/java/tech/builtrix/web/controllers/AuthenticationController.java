package tech.builtrix.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tech.builtrix.Response;
import tech.builtrix.annotations.Limited;
import tech.builtrix.annotations.NoSession;
import tech.builtrix.annotations.aspect.Countable;
import tech.builtrix.commands.SetSessionCommand;
import tech.builtrix.context.RequestContext;
import tech.builtrix.exceptions.ExceptionBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.user.User;
import tech.builtrix.services.authenticate.AuthenticationService;
import tech.builtrix.services.session.SessionKeyService;
import tech.builtrix.web.dtos.GetSessionResponse;
import tech.builtrix.web.dtos.user.LoginRequestDto;
import tech.builtrix.web.dtos.user.LoginResponseDto;
import tech.builtrix.web.dtos.user.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users/authenticate")
@Api(value = "User Controller", tags = {"User Controller"})
public class AuthenticationController {
    private final AuthenticationService service;
    private final SessionKeyService sessionKeyService;
    private final RequestContext requestContext;

    @Autowired
    public AuthenticationController(AuthenticationService service,
                                    SessionKeyService sessionKeyService,
                                    RequestContext requestContext) {
        this.service = service;
        this.sessionKeyService = sessionKeyService;
        this.requestContext = requestContext;
    }

    @ApiOperation(value = "Request for login user")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @NoSession
    public Response<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) throws ExceptionBase {
        User userLogin = this.service.loginByPassword(request.getEmail(), request.getPassword());
        String token = this.sessionKeyService.createToken(userLogin);
        UserDto user = new UserDto(userLogin);
        user.setToken(token);
        return Response.ok(new LoginResponseDto(user));
    }

    @Limited(requestsPerMinutes = 5)
    @ApiOperation(value = "sign out the device")
    @RequestMapping(value = "signout", method = RequestMethod.POST)
    @Countable("hadaf.auth.signout")
    public Response<Void> signout() throws NotFoundException {
        this.sessionKeyService.expireToken(requestContext.getSessionKey());
        return Response.ok();
    }

    @ApiOperation(value = "Get session for communicating to server")
    @RequestMapping(value = "session", method = RequestMethod.POST)
    @NoSession
    @Limited(requestsPerMinutes = 5)
    @Countable("hadaf.auth.getsession")
    public Response<GetSessionResponse> getSession(HttpServletRequest servletRequest) throws ExceptionBase {
        String sessionKey = this.sessionKeyService.createToken(null);

        Response<GetSessionResponse> response = Response.ok(new GetSessionResponse());
        response.getContent().setSession(sessionKey);
        response = response.addCommand(new SetSessionCommand(sessionKey));
        //.addCommand(new SetPushTagsCommand().add(this.pushNotificationService.getDeviceTags(device).getMap())
        //);
        return response;
    }
}
