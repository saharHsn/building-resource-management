package tech.builtrix.web.dtos.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.models.message.Message;
import tech.builtrix.web.dtos.EntityDtoBase;

@JsonInclude
@Data
@NoArgsConstructor
public class MessageDto extends EntityDtoBase {
    String id;
    String body;
    boolean isRead;

    public MessageDto(Message message) {
        this.id = message.getId();
        this.body = message.getBody();
    }
}
