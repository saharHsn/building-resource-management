package tech.builtrix.eventbus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageManager {

    private EventRepository repository;
    private ApplicationEventPublisher applicationEventPublisher;
    private ObjectMapper mapper = new ObjectMapper();
    private boolean hasNewMessage = false;

    public MessageManager(EventRepository repository, ApplicationEventPublisher applicationEventPublisher) {
        this.repository = repository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Scheduled(fixedDelay = 1000)
    private void sendMessageTask() {
       /* if (!this.hasNewMessage) {
            return;
        }*/
        List<EventEntity> unsentMessages = this.repository.findAllBySentIsFalse();
        List<EventEntity> sentMessages = new ArrayList<>(unsentMessages.size());
        for (EventEntity msg : unsentMessages) {
            try {
                Class<?> msgType = Class.forName(msg.getType());
                Object message = mapper.readValue(msg.getMsg(), msgType);
                this.applicationEventPublisher.publishEvent(message);
            } catch (ClassNotFoundException | IOException e) {
                logger.warn("", e);
            } catch (Exception e) {
                logger.error("", e);
            }
            sentMessages.add(msg);
        }
        if (sentMessages.size() > 0) {
            for (EventEntity sentMessage : sentMessages) {
                sentMessage.setSent(true);
                this.repository.save(sentMessage);
            }
        }
        this.hasNewMessage = false;

    }

    public <TMessage> void publish(TMessage msg) {
        try {
            EventEntity entity = new EventEntity();
            entity.setMsg(new ObjectMapper().writeValueAsString(msg));
            entity.setType(msg.getClass().getName());
            entity.setSent(false);
            this.repository.save(entity);
            this.hasNewMessage = true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
