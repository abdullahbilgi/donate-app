package com.project.donate.register;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRegistrationRequest {

    @NotBlank
    private String name; // Address name (örnek: "Ev Adresi", "İş Adresi")

    private String zipCode;

    private Double latitude;

    private Double longitude;

    @NotBlank
    private String cityName;

    @NotBlank
    private String regionName;
}
