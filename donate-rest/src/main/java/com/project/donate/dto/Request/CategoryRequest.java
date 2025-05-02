package com.project.donate.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryRequest {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 20, message = "Name must be between 1 and 20 characters")
    private String name;
}
