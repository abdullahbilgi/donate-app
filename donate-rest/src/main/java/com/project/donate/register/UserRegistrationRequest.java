package com.project.donate.register;

import com.project.donate.dto.AddressDTO;
import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.enums.Role;
import com.project.donate.model.Address;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String mail;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotNull
    @Min(0)
    private Integer age;

    @NotBlank
    private String phone;

    @NotNull
    private Role role;

    private AddressRequest address;

}
