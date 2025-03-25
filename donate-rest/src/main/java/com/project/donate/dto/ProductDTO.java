package com.project.donate.dto;

import com.project.donate.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 60, message = "Name must be between 1 and 60 characters")
    private String name;

    @NotNull(message = "ProductionDate is mandatory")
    private LocalDateTime productionDate;

    @NotNull(message = "ExpiryDate is mandatory")
    private LocalDateTime expiryDate;

    @NotNull(message = "Price is mandatory")
    @Min(value = 0,message = "Price must be least 0 ")
    private Double price;

    //@NotNull(message = "DiscountedPrice is mandatory")
    @Min(value = 0,message = "DiscountedPrice must be least 0 ")
    private Double discountedPrice;

    //@NotNull(message = "Discount is mandatory")
    @Min(value = 1,message = "Discount must be least 1 ")
    private Integer discount;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0,message = "Quantity must be least 0 ")
    private Integer quantity;

    @NotBlank(message = "Name is Description")
    @Size(min = 1, max = 80, message = "Dame must be between 1 and 80 characters")
    private String description;

    //@NotBlank(message = "ProductStatus is mandatory")
    private String productStatus;

    //@NotBlank(message = "ProductStatus is mandatory")
    private Boolean isActive;

    @NotBlank(message = "Category is mandatory")
    private Category category;

}
