package com.project.donate.mail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
@Validated
public class MailController {

    private final MailProducer mailProducer;

    @GetMapping("/send")
    public ResponseEntity<String> sendMail(
            @RequestParam @Email String to,
            @RequestParam @NotBlank String subject,
            @RequestParam @NotBlank String message
    ) {
        MailMessage mailMessage = new MailMessage(to, subject, message);
        mailProducer.sendToQueue(mailMessage);
        return ResponseEntity.ok("Mail queued for sending!");
    }
}
