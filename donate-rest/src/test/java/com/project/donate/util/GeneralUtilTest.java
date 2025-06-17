package com.project.donate.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneralUtilTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void extractUsername_shouldReturnAuthenticatedUsername() {
        // Arrange
        String expectedUsername = "testuser";
        var authentication = new UsernamePasswordAuthenticationToken(expectedUsername, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        String actualUsername = GeneralUtil.extractUsername();

        // Assert
        assertEquals(expectedUsername, actualUsername);
    }
}
