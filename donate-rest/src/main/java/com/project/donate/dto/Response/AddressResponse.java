package com.project.donate.dto.Response;

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
public class AddressResponse {

    private Long id;

    private String name;

    private Double latitude;

    private Double longitude;

    private String zipCode;

    private RegionResponse region;
}
