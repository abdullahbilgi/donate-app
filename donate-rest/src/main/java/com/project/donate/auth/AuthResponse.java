package com.project.donate.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.donate.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    Long userId;

    Role role;
}
