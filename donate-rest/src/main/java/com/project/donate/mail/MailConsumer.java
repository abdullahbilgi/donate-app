package com.project.donate.mail;

import com.project.donate.Config.RabbitMQConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Log4j2
public class MailConsumer {


    private final MailService mailService;

    @RabbitListener(queues = RabbitMQConfig.MAIL_QUEUE)
    public void consume(MailMessage mailMessage) {
        log.info("Received mail request for: {}", mailMessage.getTo());
        try {
            if (mailMessage.getAttachments().isEmpty()) {
                mailService.sendSimpleMessage(
                        mailMessage.getTo(),
                        mailMessage.getSubject(),
                        mailMessage.getMessage()
                );
            } else {
                mailService.sendMessageWithAttachments(
                        mailMessage.getTo(),
                        mailMessage.getSubject(),
                        mailMessage.getMessage(),
                        mailMessage.getAttachments()
                );
            }
            log.info("Mail sent successfully to: {}", mailMessage.getTo());
        } catch (MailException e) {
            log.error("Failed to send mail to {}: {}", mailMessage.getTo(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
