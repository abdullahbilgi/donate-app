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

        String message = """
                    <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:20px;border:1px solid #ddd;border-radius:10px;">
                        <h2 style="color:#2c3e50;text-align:center;">Email Verification</h2>
                        <p>Hello <b>%s</b>,</p>
                        <p>Thank you for registering. You can verify your email address using the following verification code:</p>
                        <div style="text-align:center;margin:30px 0;">
                            <span style="display:inline-block;padding:15px 30px;background-color:#3498db;color:#fff;font-size:24px;border-radius:8px;font-weight:bold;letter-spacing:4px;">
                                %s
                            </span>
                        </div>
                        <p style="color:#888;">This code will expire in 10 minutes.</p>
                        <p>Thank you,<br><b>The Donate App Team</b></p>
                    </div>
                """.formatted(user.getName(), code);


        MailMessage mailMessage = new MailMessage(user.getEmail(), "Mail Verify", message);
        log.info("Sent verification email to {}", user.getEmail());
        mailProducer.sendToQueue(mailMessage);
    }
}
