package com.project.donate.mail;

import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class VerificationService {

    private final UserRepository userRepository;
    private final MailProducer mailProducer;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendVerificationEmail(User user) {
        String code = generateVerificationCode();
        user.setVerificationCode(code);
        user.setVerificationExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        String message = "Doğrulama kodunuz: " + code;
        MailMessage mailMessage = new MailMessage(user.getEmail(), "E-posta Doğrulama", message);
        log.info("Sent verification email to {}", user.getEmail());
        mailProducer.sendToQueue(mailMessage);
    }
}
