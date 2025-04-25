package com.project.donate.dto;

import com.project.donate.enums.Status;
import com.project.donate.records.ProductItem;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CartDTO {

    private Long id;

    @NotNull(message = "User is mandatory")
    private Long userId;

    @NotNull(message = "Product items are mandatory")
    private List<ProductItem> productItems;

    private Status status;

    @NotNull(message = "TotalPrice is mandatory")
    @DecimalMin(value = "0.0", message = "TotalPrice must be at least 0")
    private Double totalPrice;

    private LocalDateTime purchaseDate;

    private LocalDateTime expiredDate;

    private Boolean isActive;
}
