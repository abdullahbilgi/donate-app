package com.project.donate.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull(message = "username can not be null")
    private String userName;
    @NotNull(message = "password can not be null")
    private String password;
}
