package com.project.donate.dto.Request;

import com.project.donate.model.Address;
import com.project.donate.model.Product;
import com.project.donate.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class MarketRequest {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 60, message = "Name must be between 1 and 60 characters")
    private String name;

    @NotBlank(message = "TaxNumber is mandatory")
    @Size(min = 5, max = 20, message = "TaxNumber must be between 5 and 20 characters")
    private String taxNumber;

    private String status;

    private Long userId;

    private String cityName;

    private String regionName;

    private String zipCode;

    private String displayName;

    private Boolean isActive;

    private Double latitude;

    private Double longitude;
}
