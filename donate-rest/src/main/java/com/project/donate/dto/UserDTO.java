package com.project.donate.dto;

import com.project.donate.enums.Role;
import com.project.donate.model.Address;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only letters")
    private String name;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 60, message = "Username must be between 3 and 60 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 3, max = 60, message = "Password must be between 3 and 60 characters")
    private String password;

    @NotBlank(message = "Surname is mandatory")
    @Size(min = 3, max = 60, message = "Surname must be between 3 and 60 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only letters")
    private String surname;

    @NotBlank(message = "Email is mandatory")
    @Size(min = 5, max = 60, message = "Email must be between 5 and 60 characters")
    @Email
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Size(min = 1, max = 14, message = "Phone must be between 5 and 14 characters")
    private String phone;

    @NotNull(message = "Age is mandatory")
    @Min(value = 0,message = "Age must be least 0 ")
    private Integer age;

    @NotBlank(message = "Role is mandatory")
    private String role;

    @NotBlank(message = "Address is mandatory")
    private Address address;
}
