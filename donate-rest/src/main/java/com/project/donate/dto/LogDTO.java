package com.project.donate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LogDTO {

    private Long id;

    @NotNull(message = "DateTime is mandatory")
    private LocalDateTime dateTime;

    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotBlank(message = "Function is mandatory")
    private String function;

    @NotBlank(message = "Service is mandatory")
    private String service;

    @NotBlank(message = "Message is mandatory")
    private String message;
}
