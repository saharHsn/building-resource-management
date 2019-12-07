package tech.builtrix.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tech.builtrix.Response;
import tech.builtrix.dtos.user.LoginRequestDto;
import tech.builtrix.dtos.user.LoginResponseDto;
import tech.builtrix.dtos.user.UserDto;
import tech.builtrix.exceptions.InactiveUserException;
import tech.builtrix.exceptions.InvalidPasswordException;
import tech.builtrix.models.user.User;
import tech.builtrix.services.authenticate.AuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/users/authenticate")
@Api(value = "User Controller", tags = {"User Controller"})
public class AuthenticationController {
    private final AuthenticationService service;

    @Autowired
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @ApiOperation(value = "Request for login user")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Response<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) throws InactiveUserException, InvalidPasswordException {
        User userLogin = this.service.loginByPassword(request.getEmail(), request.getPassword());
        return Response.ok(new LoginResponseDto(new UserDto(userLogin)));
    }
}
