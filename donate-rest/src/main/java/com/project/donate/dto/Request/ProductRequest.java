package com.project.donate.dto.Request;

import com.project.donate.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 60, message = "Name must be between 1 and 60 characters")
    private String name;

    @NotNull(message = "ProductionDate is mandatory")
    private LocalDateTime productionDate;

    @NotNull(message = "ExpiryDate is mandatory")
    private LocalDateTime expiryDate;

    @NotNull(message = "LastDonatedDate is mandatory")
    private LocalDateTime lastDonatedDate;

    @Min(value = 0, message = "Price must be at least 0")
    private Double price;

    @Min(value = 0, message = "DiscountedPrice must be at least 0")
    private Double discountedPrice;

    @NotNull(message = "Status is mandatory")
    private ProductStatus productStatus;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 1, max = 80, message = "Description must be between 1 and 80 characters")
    private String description;

    private Long categoryId;

    private Long marketId;
}
