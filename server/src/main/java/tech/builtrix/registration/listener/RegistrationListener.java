package tech.builtrix.registration.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import tech.builtrix.common.service.EmailSender;
import tech.builtrix.model.user.TokenPurpose;
import tech.builtrix.model.user.User;
import tech.builtrix.model.user.UserToken;
import tech.builtrix.registration.OnRegistrationCompleteEvent;
import tech.builtrix.service.authenticate.CodeService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final CodeService codeService;
    private final MessageSource messages;
    private final EmailSender mailSender;
    private final Environment env;

    public RegistrationListener(CodeService codeService,
                                MessageSource messages,
                                EmailSender mailSender,
                                Environment env) {
        this.codeService = codeService;
        this.messages = messages;
        this.mailSender = mailSender;
        this.env = env;
    }

    // API
    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException: " + e);
        } catch (MessagingException e) {
            logger.error("MessagingException: " + e);
        }
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) throws UnsupportedEncodingException, MessagingException {
        final User user = event.getUser();
        UserToken token = codeService.createToken(user, TokenPurpose.Register);

        final SimpleMailMessage email = constructEmailMessage(event, user, token.getToken());
        mailSender.sendEmail(email);
    }

    //

    private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmailAddress();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
        final String message = messages.getMessage("message.regSucc", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
