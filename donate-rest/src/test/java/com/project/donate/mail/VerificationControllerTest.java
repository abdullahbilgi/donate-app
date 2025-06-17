package com.project.donate.mail;

import com.project.donate.Jwt.Token.TokenRepository;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@Import(com.project.donate.config.NoSecurityConfig.class)
@WebMvcTest(VerificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private com.project.donate.Jwt.JWTService jwtService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VerificationService verificationService;

    @Test
    void testVerifyCodeSuccess() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        user.setVerificationCode("123456");
        user.setVerificationExpiry(LocalDateTime.now().plusMinutes(5));
        user.setEmailVerified(false);

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        mockMvc.perform(post("/api/v1/verify")
                        .param("email", "user@example.com")
                        .param("code", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("Verify successful."));
    }

    @Test
    void testSendVerifyCode() throws Exception {
        User user = new User();
        user.setEmail("user@example.com");
        user.setEmailVerified(false);

        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        mockMvc.perform(post("/api/v1/verify/sendCode")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk());

        verify(verificationService).sendVerificationEmail(user);
    }
}
