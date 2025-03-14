package com.project.donate.dto;

import com.project.donate.model.Address;
import com.project.donate.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrganizationDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 60, message = "Name must be between 5 and 60 characters")
    private String name;

    @NotBlank(message = "TaxNumber is mandatory")
    @Size(min = 5, max = 20, message = "TaxNumber must be between 5 and 20 characters")
    private String taxNumber;

    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotBlank(message = "User is mandatory")
    private User user;

    @NotBlank(message = "Address is mandatory")
    private Address address;
}
