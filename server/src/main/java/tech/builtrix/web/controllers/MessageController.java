package tech.builtrix.web.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.models.user.User;
import tech.builtrix.services.message.MessageService;
import tech.builtrix.web.dtos.message.MessageDto;

import java.util.List;

@RestController
@RequestMapping("/v1/messages")
@Api(value = "User Controller", tags = {"Message Controller"})
public class MessageController extends ControllerBase {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @ApiOperation(value = "Request for creating new message")
    @PostMapping(value = "{buildingId}")
    public Response<MessageDto> create(@PathVariable("buildingId") String buildingId, @RequestParam String message) {
        MessageDto messageDto;
        messageDto = messageService.save(buildingId, message);
        return Response.ok(messageDto);
    }

    @ApiOperation(value = "Request for updating a specific building")
    @PostMapping(value = "/updateReadStatus")
    public Response<Void> updateReadStatus(@RequestParam String messageId, @RequestParam boolean readStatus) {
        User user = this.requestContext.getUser();
        this.messageService.updateReadStatus(user, messageId, readStatus);
        return Response.ok();
    }

    @ApiOperation(value = "Request for getting all buildings")
    @GetMapping(value = "{buildingId}")
    public Response<List<MessageDto>> getAllBuildingMessages(@PathVariable("buildingId") String buildingId) {
        User user = this.requestContext.getUser();
        List<MessageDto> allBuildingMessages = this.messageService.getAll(user, buildingId);
        return Response.ok(allBuildingMessages);
    }
}
