package com.project.donate.dto.Response;


import com.project.donate.model.Cart;
import com.project.donate.model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddToCartResponse {
    //private CartResponse cart;

    private ProductResponse product;

    private Integer productQuantity;
}
