package com.project.donate.dto;

import com.project.donate.model.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegionDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 60, message = "Name must be between 1 and 60 characters")
    private String name;

    @NotBlank(message = "City is mandatory")
    private City city;

    private Long cityId;
}
