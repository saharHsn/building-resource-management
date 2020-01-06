package tech.builtrix.registration.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import tech.builtrix.commons.EmailSender;
import tech.builtrix.exceptions.session.InternalServerException;
import tech.builtrix.models.user.TokenPurpose;
import tech.builtrix.models.user.User;
import tech.builtrix.models.user.VerificationToken;
import tech.builtrix.registration.OnRegistrationCompleteEvent;
import tech.builtrix.services.authenticate.CodeService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

@Component
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final CodeService codeService;
    private final MessageSource messages;
    private final EmailSender mailSender;
    private final Environment env;
    private final ResourceLoader res;

    public RegistrationListener(CodeService codeService,
                                MessageSource messages,
                                EmailSender mailSender,
                                Environment env,
                                ResourceLoader res) {
        this.codeService = codeService;
        this.messages = messages;
        this.mailSender = mailSender;
        this.env = env;
        this.res = res;
    }

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException: " + e);
        } catch (MessagingException e) {
            logger.error("MessagingException: " + e);
        } catch (InternalServerException e) {
            logger.error("InternalServerException: " + e);
        }
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) throws UnsupportedEncodingException, MessagingException, InternalServerException {
        final User user = event.getUser();
        VerificationToken token = codeService.createToken(user, TokenPurpose.Register);

        final SimpleMailMessage email = constructEmailMessage(event, user, token.getToken());
        mailSender.sendEmail(email);
    }

    private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        String template = null;
        try {
            InputStream inputStream = res.getResource("classpath:/templates/email/registration.confirmation.template").getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            template = scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            logger.error("Can not read email templates" + e.toString());
        }
        String recipientAddress = user.getEmailAddress();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/v1/users/registrationConfirm?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        // email.setText(message + " \r\n" + confirmationUrl);
        email.setText(template != null ? template.replace("{555}", confirmationUrl).replace("{user}", user.getFirstName()) : null);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
