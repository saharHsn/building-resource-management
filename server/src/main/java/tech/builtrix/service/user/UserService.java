package tech.builtrix.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dto.UserDto;
import tech.builtrix.exception.*;
import tech.builtrix.model.user.Role;
import tech.builtrix.model.user.TokenPurpose;
import tech.builtrix.model.user.User;
import tech.builtrix.model.user.UserToken;
import tech.builtrix.repository.user.UserRepository;
import tech.builtrix.service.authenticate.CodeService;
import tech.builtrix.service.authenticate.ValidationService;

import javax.transaction.Transactional;
import java.util.Optional;


@Component
@Slf4j
public class UserService extends GenericCrudServiceBase<User, UserRepository> {
    private final CodeService codeService;
    private final ValidationService validationService;
    @Autowired
    public UserService(UserRepository repository,
                       CodeService codeService,
                       ValidationService validationService) {
        super(repository);
        this.codeService = codeService;
        this.validationService = validationService;
    }


    public UserDto findById(String id) throws NotFoundException {
        Optional<User> optionalUser = this.repository.findById(id);
        if (optionalUser.isPresent()) {
            return new UserDto(optionalUser.get());
        } else {
            throw new NotFoundException("user", "id", id);
        }
    }

    public User save(UserDto userDto) {
        User user = new User(userDto);
        user = this.repository.save(user);
        return user;
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

    public User getEmailAndUser(String email) throws NotFoundException{
        return null;
    }

    @Transactional
    public String registerUser(String firstName,
                               String lastName,
                               String email,
                               String password,
                               String confirmPassword,
                               String sessionKey) throws ExceptionBase {
        User user = updateUserByEmail(firstName, lastName, email, password, confirmPassword);
        UserToken userToken;
        userToken = this.codeService.createEmailToken(user, TokenPurpose.Register);
        return userToken.getValue();
    }

    @Transactional
    public User updateUserByEmail(String firstName,
                                  String lastName,
                                  String email,
                                  String password,
                                  String confirmPassword) throws ExceptionBase {
        //just reload user
        User user = this.getEmailAndUser(email);
        if (user != null) {
            throw new AlreadyExistException("emailAddress", email);
        }
        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordConfirmationException();
        }
        if (!this.validationService.validateEmail(email)) {
            throw new InvalidEmailException();
        }
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmailAddress(email);
        userDto.setRole(Role.User);
        userDto.setPassword(password);
        return this.save(userDto);
    }
   /* @Transactional
    public void addSuccessfulLogin(User user, String reason) {
        User dbUser = this.repository.findById(user.getId()).get();
        UserLogin login = new UserLogin();
        login.setUser(dbUser);
        login.setMessage(reason);
        login.setStatus(true);
        dbUser.setFailedLogin((byte) 0);
        dbUser.setLockedOutEndTime(null);
        dbUser.getLogins().add(login);
        this.repository.save(dbUser);
    }

    @Transactional
    public void addFailedLogin(User user, String reason) {
        User dbUser = this.repository.findById(user.getId()).get();
        UserLogin login = new UserLogin();
        login.setUser(dbUser);
        login.setMessage(reason);
        login.setStatus(false);
        dbUser.getLogins().add(login);
        dbUser.setFailedLogin((byte) (dbUser.getFailedLogin() + 1));
        if (dbUser.getFailedLogin() > MaxFailedLogin) {
            dbUser.setLockedOutEndTime(DateHelper.utcNowAddSeconds(LockedOutSeconds));
        }
        this.repository.save(dbUser);
    }*/
}
