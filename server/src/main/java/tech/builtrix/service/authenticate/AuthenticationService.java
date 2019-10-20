package tech.builtrix.service.authenticate;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import tech.builtrix.dto.UserDto;
import tech.builtrix.exception.InactiveUserException;
import tech.builtrix.exception.InvalidPasswordConfirmationException;
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

        if (!StringUtils.isEmpty(user.getPassword()) &&
                user.getPassword().equals(password)) {
            //this.userService.addSuccessfulLogin(user, "Email/Password");
            return user;
        } else {
            //this.userService.addFailedLogin(user, "Invalid password");
            throw new InvalidPasswordException();
        }
    }

    public boolean resetPassword(UserDto user) {
        String password = RandomString.make(8);
        String hashPassword = HashUtil.sha1(password);
        user.setPassword(hashPassword);
        //user.setEmailConfirmed(false);
        userService.update(user);
        /*String body = messageSource.getMessage("email.reset_password.template",
                new Object[]{password},
                LocaleContextHolder.getLocale());
        String subject = "change password";
        String sender = "investment.efarda.ir";
        this.emailService.sendEmail(user.getEmailAddress(), subject, body, true, sender);*/
        return true;
    }

    public boolean changePassword(UserDto user,
                                  String oldPassword,
                                  String newPassword,
                                  String confirmNewPassword) throws
            InvalidPasswordException,
            InvalidPasswordConfirmationException {
        String hashOldPassword = HashUtil.sha1(oldPassword);

        if (!user.getPassword().equals(hashOldPassword)) {
            throw new InvalidPasswordException();
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new InvalidPasswordConfirmationException();
        }

        String hashNewPassword = HashUtil.sha1(newPassword);
        user.setPassword(hashNewPassword);
        userService.update(user);

        return true;
    }

}
