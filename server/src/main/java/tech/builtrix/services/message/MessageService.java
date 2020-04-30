package tech.builtrix.services.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.models.message.Message;
import tech.builtrix.models.user.User;
import tech.builtrix.repositories.message.MessageRepository;
import tech.builtrix.web.dtos.message.MessageDto;

import java.util.*;

@Component
@Slf4j
public class MessageService extends GenericCrudServiceBase<Message, MessageRepository> {
    protected MessageService(MessageRepository repository) {
        super(repository);
    }

    public List<MessageDto> getAll(User user, String buildingId) {
        List<MessageDto> messageDtoList = new ArrayList<>();
        List<Message> allBuildingMessages = this.repository.findByOwnerBuilding(buildingId);
        for (Message message : allBuildingMessages) {
            MessageDto dto = new MessageDto();
            Map<String, Boolean> usersStatus = message.getUsersStatus();
            dto.setMessage(message.getBody());
            dto.setId(message.getId());
            if (usersStatus.containsKey(user.getId())) {
                dto.setRead(usersStatus.get(user.getId()));
            } else {
                logger.warn("user read status is not present!");
                dto.setRead(false);
            }
            messageDtoList.add(dto);
        }
        return messageDtoList;
    }

    public MessageDto save(String buildingId, String body) {
        Message message = new Message();
        message.setOwnerBuilding(buildingId);
        message.setBody(body);
        message.setUsersStatus(new HashMap<>());
        this.repository.save(message);
        return new MessageDto(message);
    }

    public void updateReadStatus(User user, String messageId, boolean readStatus) {
        Optional<Message> messageOptional = this.repository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.getUsersStatus().put(user.getId(), readStatus);
            this.repository.save(message);
        }
    }
}
