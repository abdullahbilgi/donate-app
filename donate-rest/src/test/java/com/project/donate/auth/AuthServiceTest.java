package com.project.donate.auth;

import com.project.donate.Jwt.JWTService;
import com.project.donate.Jwt.Token.TokenRepository;
import com.project.donate.Jwt.Token.TokenType;
import com.project.donate.exception.EmailNotVerifiedException;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldThrowException_ifEmailNotVerified() {
        AuthRequest request = new AuthRequest();
        request.setUserName("test");
        request.setPassword("password");

        User user = new User();
        user.setUsername("test");
        user.setEmailVerified(false);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        assertThrows(EmailNotVerifiedException.class, () -> authService.login(request));
    }

    @Test
    void login_shouldReturnAuthResponse_whenValidCredentials() {
        AuthRequest request = new AuthRequest();
        request.setUserName("test");
        request.setPassword("password");

        User user = new User();
        user.setUsername("test");
        user.setId(1L);
        user.setEmailVerified(true);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(java.util.Collections.emptyList());
        when(tokenRepository.findByToken("access-token")).thenReturn(Optional.empty());

        AuthResponse response = authService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(1L, response.getUserId());
    }

}
