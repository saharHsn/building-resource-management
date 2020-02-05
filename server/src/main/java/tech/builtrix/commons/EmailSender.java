package tech.builtrix.commons;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import tech.builtrix.exceptions.session.InternalServerException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public interface EmailSender {
	void sendEmail(String sender, String receiver, String title, String content, Boolean isHtml)
			throws UnsupportedEncodingException, MessagingException, InternalServerException;

	void sendEmail(SimpleMailMessage email)
			throws UnsupportedEncodingException, MessagingException, InternalServerException;
}
