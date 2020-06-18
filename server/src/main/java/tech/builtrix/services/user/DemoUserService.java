package tech.builtrix.services.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.models.user.DemoUser;
import tech.builtrix.repositories.user.DemoUserRepository;
import tech.builtrix.web.dtos.emailToken.RegisterDemoUserDto;

import javax.validation.Valid;
import java.util.List;

@Component
@Slf4j
public class DemoUserService extends GenericCrudServiceBase<DemoUser, DemoUserRepository> {
    protected DemoUserService(DemoUserRepository repository) {
        super(repository);
    }

    public DemoUser registerDemoUser(@Valid RegisterDemoUserDto registerUserDto) {
        List<DemoUser> demoUsers = repository.findByEmailAddress(registerUserDto.getEmailAddress());
        if (!CollectionUtils.isEmpty(demoUsers)) {
            return demoUsers.get(0);
        }
        final DemoUser user = new DemoUser();
        user.setFullName(registerUserDto.getFullName());
        user.setEmailAddress(registerUserDto.getEmailAddress());
        return repository.save(user);
    }
}
