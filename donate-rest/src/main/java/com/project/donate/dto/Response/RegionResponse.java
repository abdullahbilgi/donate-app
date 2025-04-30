package com.project.donate.dto.Response;

import com.project.donate.model.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegionResponse {

    private Long id;

    private String name;

    private CityResponse city;
}
