package tech.builtrix;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.message.Message;
import tech.builtrix.models.user.User;
import tech.builtrix.services.message.MessageService;
import tech.builtrix.services.user.UserService;
import tech.builtrix.web.dtos.message.MessageDto;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class DataEntryTest {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Test
    public void createNewMessage() {
        // demo : "id": "fae0c9a2-ef89-477a-a073-a9e704e5ccb3",
        // franklin "id": "9d94dd4d-b789-4717-bdee-517a8de8ca6e",
        // parede "id": "4f9e5bc1-471d-4b37-87ca-82f803898bb6",
        // this.messageService.save("9d94dd4d-b789-4717-bdee-517a8de8ca6e", "Your load factor is below 15% (12% in average) which means your building has high potential for energy efficiency actions.\n");
        this.messageService.save("9d94dd4d-b789-4717-bdee-517a8de8ca6e", "Your consumption in Winter is 38% more than Summer.\n");
        this.messageService.save("9d94dd4d-b789-4717-bdee-517a8de8ca6e", "Your consumption in March 2020 has increased by 20% compared to 2019 while the average temperature is constant at 14 deg. C\n");

        this.messageService.save("4f9e5bc1-471d-4b37-87ca-82f803898bb6", "Your load factor is below 15% (8% in average) which means your building has high potential for energy efficiency actions.");
        this.messageService.save("4f9e5bc1-471d-4b37-87ca-82f803898bb6", "Your consumption in Winter is 20% more than Summer.\n");
        this.messageService.save("4f9e5bc1-471d-4b37-87ca-82f803898bb6", "Your consumption in March 2020 has increased by 18% compared to 2019 while the average temperature is constant at 14 deg. C\n");
    }

    @Test
    public void updateMessage() throws NotFoundException {
        User user = this.userService.findById("9289da95-0b63-40de-a01c-7f19fb241156");
        //this.messageService.updateReadStatus(user, "3f9241b5-5541-4359-940a-eee5f20d5c30", false);
        this.messageService.updateReadStatus(user, "3f9241b5-5541-4359-940a-eee5f20d5c30", true);
        Message message = this.messageService.getById("3f9241b5-5541-4359-940a-eee5f20d5c30");
        System.out.println(message.getUsersStatus());
        this.messageService.updateReadStatus(user, "3f9241b5-5541-4359-940a-eee5f20d5c30", false);
        Message message2 = this.messageService.getById("3f9241b5-5541-4359-940a-eee5f20d5c30");
        System.out.println(message2.getUsersStatus());
        //this.messageService.updateReadStatus(user, "3f9241b5-5541-4359-940a-eee5f20d5c30", false);
    }

    @Test
    public void getAllBuildingMessages() throws NotFoundException {
        User user = this.userService.findById("9289da95-0b63-40de-a01c-7f19fb241156");
        List<MessageDto> all = this.messageService.getAll(user, "9d94dd4d-b789-4717-bdee-517a8de8ca6e");
        System.out.println();
    }
}
