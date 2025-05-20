package com.project.donate.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendToQueue(MailMessage message) {
        rabbitTemplate.convertAndSend("mail-queue", message);
    }
}
