package com.project.donate.mail;

import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/verify")
public class VerificationController {

    private final UserRepository userRepository;
    private final VerificationService verificationService;

    @PostMapping
    public ResponseEntity<String> verifyCode(@RequestParam @Email String email, @RequestParam String code) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User not found with email: {}", email);
            throw new RuntimeException("User not found");
        }

        if (user.getEmailVerified()) {
            log.warn("Already verified email: {}", email);
            return ResponseEntity.ok("Already verified.");
        }

        if (user.getVerificationCode().equals(code) && user.getVerificationExpiry().isAfter(LocalDateTime.now())) {
            user.setEmailVerified(true);
            user.setVerificationCode(null);
            user.setVerificationExpiry(null);
            userRepository.save(user);
            log.info("{} mail address verified.", user.getEmail());
            return ResponseEntity.ok("Verify successful.");
        } else {
            return ResponseEntity.badRequest().body("Code wrong or expired.");
        }
    }

    @PostMapping("/sendCode")
    public ResponseEntity<String> sendVerifyCode(@RequestParam @Email String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User not found with email: {}", email);
            throw new RuntimeException("User not found");
        }

        if (user.getEmailVerified()) {
            log.warn("Already verified email: {}", email);
            return ResponseEntity.ok("Already verified.");
        }

        verificationService.sendVerificationEmail(user);
        log.info("{} sent mail for verified.", user.getEmail());
        return ResponseEntity.ok("Send verification mail successful.");



    }


}
