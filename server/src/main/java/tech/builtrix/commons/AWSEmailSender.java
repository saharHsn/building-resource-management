package tech.builtrix.commons;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Component
@Profile({"dev", "prod"})
@Slf4j
@ConfigurationProperties("metrics.email.aws")
public class AWSEmailSender implements EmailSender {

    @Setter
    private String server;
    @Setter
    private String userName;
    @Setter
    private String password;
    @Setter
    private String defaultAccount;
    @Setter
    private int port;

    @Override
    public void sendEmail(String sender, String receiver, String title, String content, Boolean isHtml) throws UnsupportedEncodingException, MessagingException {
        logger.info("trying to send email for email : " + receiver);
        if (StringUtils.isEmpty(sender)) {
            sender = this.defaultAccount;
        }
        sendMail(sender, receiver, title, content);
    }

    @Override
    public void sendEmail(SimpleMailMessage email) throws UnsupportedEncodingException, MessagingException {
        this.sendEmail(email.getFrom(), email.getTo()[0], email.getSubject(), email.getText(), true);
    }

    void sendMail(String fromUserEmail, String toEmail, String subject, String body) {
        try {
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromUserEmail, "fromUserFullName"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");

            Transport transport = session.getTransport();
            transport.connect(server, userName, password);
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
