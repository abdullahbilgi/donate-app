package com.project.donate.register;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        registerService.register(request);
        return ResponseEntity.ok("User registered successfully.");
    }
}
