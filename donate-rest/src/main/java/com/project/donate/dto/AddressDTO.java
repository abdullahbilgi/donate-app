package com.project.donate.dto;

import com.project.donate.model.City;
import com.project.donate.model.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 80, message = "Name must be between 3 and 80 characters")
    private String name;

    @NotNull(message = "Latitude is mandatory")
    private Double latitude;

    @NotNull(message = "Longitude is mandatory")
    private Double longitude;

    @NotBlank(message = "ZipCode is mandatory")
    @Size(min = 5, max = 10, message = "ZipCode must be between 5 and 10 characters")
    private String zipCode;

    @NotBlank(message = "Region is mandatory")
    private Region region;
}