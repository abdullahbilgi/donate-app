package com.project.donate.mail;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MailServiceTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);
    private final MailService mailService = new MailService(mailSender);

    @Test
    void testSendSimpleMessageCallsWithAttachmentMethod() {
        MailService spyService = spy(mailService);
        doNothing().when(spyService).sendMessageWithAttachments(any(), any(), any(), isNull());

        spyService.sendSimpleMessage("losev.app54@gmail.com", "Subject", "Message");

        verify(spyService).sendMessageWithAttachments(eq("losev.app54@gmail.com"), eq("Subject"), eq("Message"), isNull());
    }

    @Test
    void testSendMessageWithAttachmentsSuccess() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService.sendMessageWithAttachments("losev.app54@gmail.com", "Subject", "<b>HTML</b>", null);

        verify(mailSender, times(1)).send(mimeMessage);
    }
}
