package com.project.donate.mail;

import com.project.donate.Jwt.Token.TokenRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@Import(com.project.donate.config.NoSecurityConfig.class)
@WebMvcTest(MailController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private com.project.donate.Jwt.JWTService jwtService;

    @MockBean
    private MailProducer mailProducer;

    @Test
    void testSendMailSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/mail/send")
                        .param("to", "losev.app54@gmail.com")
                        .param("subject", "Subject")
                        .param("message", "Body"))
                .andExpect(status().isOk());

        verify(mailProducer, times(1)).sendToQueue(any(MailMessage.class));
    }
}
