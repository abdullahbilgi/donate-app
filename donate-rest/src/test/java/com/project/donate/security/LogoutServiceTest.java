package com.project.donate.security;

import com.project.donate.Jwt.Token.Token;
import com.project.donate.Jwt.Token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LogoutServiceTest {

    private TokenRepository tokenRepository;
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(TokenRepository.class);
        logoutService = new LogoutService(tokenRepository);
    }

    @Test
    void shouldLogoutAndInvalidateToken() {
        // given
        String jwt = "sample.jwt.token";
        String authHeader = "Bearer " + jwt;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", authHeader);
        HttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        Token token = new Token();
        token.setToken(jwt);
        token.setExpired(false);
        token.setRevoked(false);

        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.of(token));

        // when
        logoutService.logout(request, response, authentication);

        // then
        assertThat(token.isExpired()).isTrue();
        assertThat(token.isRevoked()).isTrue();
        verify(tokenRepository, times(1)).save(token);
    }

    @Test
    void shouldNotLogoutWhenAuthorizationHeaderIsMissing() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        // when
        logoutService.logout(request, response, authentication);

        // then
        verify(tokenRepository, never()).findByToken(anyString());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void shouldNotLogoutWhenTokenNotFound() {
        // given
        String jwt = "unknown.jwt.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwt);
        HttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.empty());

        // when
        logoutService.logout(request, response, authentication);

        // then
        verify(tokenRepository, times(1)).findByToken(jwt);
        verify(tokenRepository, never()).save(any());
    }
}
