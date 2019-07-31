package tech.builtrix.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dto.UserDto;
import tech.builtrix.exception.NotFoundException;
import tech.builtrix.model.user.User;
import tech.builtrix.repository.user.UserRepository;

import java.util.Optional;


@Component
@Slf4j
public class UserService extends GenericCrudServiceBase<User, UserRepository> {

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }

    public UserDto findById(String id) throws NotFoundException {
        Optional<User> optionalUser = this.repository.findById(id);
        if (optionalUser.isPresent()) {
            return new UserDto(optionalUser.get());
        } else {
            throw new NotFoundException("user", "id", id);
        }
    }

    public String save(UserDto userDto) {
        User user = new User(userDto);
        user = this.repository.save(user);
        return user.getId();
    }

    public void update(UserDto user) {
        save(user);
    }

    public void delete(String userId) {
        this.repository.deleteById(userId);
    }

    public void deleteAll() {
        this.repository.deleteAll();
    }

    public Iterable<User> findAll() {
        return this.repository.findAll();
    }

}
