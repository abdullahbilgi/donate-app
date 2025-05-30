package com.project.donate.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailService {

    private final JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) throws MailException {
        sendMessageWithAttachments(to, subject, text, null);
    }

    public void sendMessageWithAttachments(String to, String subject, String htmlContent,
                                           Map<String, byte[]> attachments) throws MailException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("donate.app54@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML içeriği burada set ediliyor

            if (attachments != null) {
                for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                    helper.addAttachment(entry.getKey(), new ByteArrayResource(entry.getValue()));
                }
            }

            mailSender.send(message);
            log.debug("Email sent to {} with subject '{}'", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new MailException("Failed to send email: " + e.getMessage()) {};
        }
    }
}
