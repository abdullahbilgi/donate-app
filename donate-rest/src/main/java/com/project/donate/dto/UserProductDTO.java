package com.project.donate.dto;

import com.project.donate.model.Product;
import com.project.donate.model.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserProductDTO {

    private Long id;

    @NotBlank(message = "User is mandatory")
    private User user;

    @NotBlank(message = "Product is mandatory")
    private Product product;

    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0, message = "Quantity must be least 0 ")
    private Integer quantity;

    @NotNull(message = "TotalPrice is mandatory")
    @Min(value = 0, message = "TotalPrice must be least 0 ")
    private Double totalPrice;


}
