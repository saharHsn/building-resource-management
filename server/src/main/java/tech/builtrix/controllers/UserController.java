package tech.builtrix.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.dtos.user.UserDto;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.services.user.UserService;

/**
 * Created By sahar-hoseini at 08. Jul 2019 5:52 PM
 **/
@RestController
@RequestMapping("/v1/users")
@Api(value = "User Controller", tags = {"User Controller"})
public class UserController extends ControllerBase {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @ApiOperation(value = "Request for creating new user")
    @PostMapping
    public Response<String> save(@RequestBody UserDto user) {
        String userId = service.save(user).getId();
        return Response.ok(userId);
    }

    @ApiOperation(value = "Request for getting a owner details")
    @GetMapping(value = "{userId}")
    public Response<UserDto> getById(@PathVariable("userId") String userId) throws NotFoundException {
        UserDto user = service.findById(userId);
        return Response.ok(user);
    }

    @ApiOperation(value = "Request for updating a specific user")
    @PutMapping
    public Response<Void> update(@RequestBody UserDto user) {
        service.update(user);
        return Response.ok();
    }

    @ApiOperation(value = "Request for deleting a specific user")
    @DeleteMapping(value = "{userId}")
    public Response<Void> delete(@PathVariable("userId") String userId) {
        service.delete(userId);
        return Response.ok();
    }
}
