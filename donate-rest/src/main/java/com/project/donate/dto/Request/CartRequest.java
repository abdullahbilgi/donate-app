package com.project.donate.dto.Request;

import com.project.donate.enums.Status;
import com.project.donate.records.ProductItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CartRequest {

    private Long id;

    @NotNull(message = "User is mandatory")
    private Long userId;
}
