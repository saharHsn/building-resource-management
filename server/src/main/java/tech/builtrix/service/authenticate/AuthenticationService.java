package tech.builtrix.service.authenticate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.exception.InactiveUserException;
import tech.builtrix.exception.InvalidPasswordException;
import tech.builtrix.exception.NotFoundException;
import tech.builtrix.model.user.User;
import tech.builtrix.service.user.UserService;
import tech.builtrix.util.HashUtil;

/**
 * Created By sahar at 10/17/19
 */
@Component
@Slf4j
public class AuthenticationService {
    private final UserService userService;

    @Autowired
    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public User loginByPassword(String email, String password) throws
            InvalidPasswordException,
            InactiveUserException {
        User user;
        try {
            user = this.userService.findByEmail(email);
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
