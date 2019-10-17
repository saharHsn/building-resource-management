package tech.builtrix.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.dto.LoginRequest;
import tech.builtrix.dto.LoginResponse;
import tech.builtrix.dto.UserDto;
import tech.builtrix.dto.emailToken.EmailTokenRequest;
import tech.builtrix.dto.emailToken.EmailTokenResponse;
import tech.builtrix.exception.ExceptionBase;
import tech.builtrix.exception.InactiveUserException;
import tech.builtrix.exception.InvalidPasswordException;
import tech.builtrix.limit.Limited;
import tech.builtrix.model.user.User;
import tech.builtrix.service.authenticate.AuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users/authenticate")
@Api(value = "User Controller", tags = {"User Controller"})
public class AuthenticationController {
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @ApiOperation(value = "Request for login user")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws InactiveUserException, InvalidPasswordException {
        User userLogin = this.service.loginByPassword(request.getEmail(), request.getPassword());
        return Response.ok(new LoginResponse(new UserDto(userLogin)));
    }
}
