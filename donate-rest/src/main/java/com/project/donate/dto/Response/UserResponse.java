package com.project.donate.dto.Response;

import com.project.donate.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String username;

    private String surname;

    private String email;

    private String phone;

    private Integer age;

    private Role role;

    private AddressResponse address;
}
