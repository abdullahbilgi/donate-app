package com.project.donate.mail;

import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class VerificationServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final MailProducer mailProducer = mock(MailProducer.class);

    private final VerificationService service = new VerificationService(userRepository, mailProducer);

    @Test
    void testSendVerificationEmail() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");

        service.sendVerificationEmail(user);

        verify(userRepository).save(user);
        verify(mailProducer).sendToQueue(any(MailMessage.class));
        assert user.getVerificationExpiry().isAfter(LocalDateTime.now());
    }

    @Test
    void testGenerateCodeFormat() {
        String code = service.generateVerificationCode();
        assert code.matches("\\d{6}");
    }
}
