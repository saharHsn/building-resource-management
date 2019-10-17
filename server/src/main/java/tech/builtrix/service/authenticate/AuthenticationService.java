package tech.builtrix.service.authenticate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.builtrix.dto.UserDto;
import tech.builtrix.exception.*;
import tech.builtrix.model.user.Role;
import tech.builtrix.model.user.TokenPurpose;
import tech.builtrix.model.user.User;
import tech.builtrix.model.user.UserToken;
import tech.builtrix.service.user.UserService;
import tech.builtrix.util.HashUtil;

import javax.transaction.Transactional;

/**
 * Created By sahar at 10/17/19
 */
@Service
@Slf4j
public class AuthenticationService {
    private final UserService userService;

    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public User loginByPassword(String email, String password) throws
            InvalidPasswordException,
            InactiveUserException {
        User user;
        try {
            user = this.userService.getEmailAndUser(email);
        } catch (NotFoundException ex) {
            throw new InvalidPasswordException();
        }

        if (!user.getActive()) {
            throw new InactiveUserException();
        }

        password = HashUtil.sha1(password);

        if (user.getPassword().equals(password)) {
            //this.userService.addSuccessfulLogin(user, "Email/Password");
            return user;
        } else {
            //this.userService.addFailedLogin(user, "Invalid password");
            throw new InvalidPasswordException();
        }
    }
}
