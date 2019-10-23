package tech.builtrix.common.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public interface EmailSender {
    void sendEmail(String sender, String receiver, String title, String content, Boolean isHtml) throws UnsupportedEncodingException, MessagingException;

    void sendEmail(SimpleMailMessage email) throws UnsupportedEncodingException, MessagingException;
}
