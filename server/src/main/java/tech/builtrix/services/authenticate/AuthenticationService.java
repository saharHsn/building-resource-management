package tech.builtrix.services.authenticate;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.exceptions.InactiveUserException;
import tech.builtrix.exceptions.InvalidPasswordConfirmationException;
import tech.builtrix.exceptions.InvalidPasswordException;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.user.User;
import tech.builtrix.services.user.UserService;
import tech.builtrix.utils.HashUtil;
import tech.builtrix.web.dtos.user.UserDto;

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

        if (!user.getEnabled()) {
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

    public boolean resetPassword(UserDto user) throws NotFoundException {
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
            InvalidPasswordConfirmationException, NotFoundException {
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
