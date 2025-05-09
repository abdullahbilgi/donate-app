package com.project.donate.dto.Response;

import com.project.donate.model.Product;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartProductResponse {
    private ProductResponse productResponse;
    private Double productPrice;
    private Integer productQuantity;


    public CartProductResponse(ProductResponse productResponse, Double productPrice, Integer productQuantity) {
        this.productResponse = productResponse;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }
}
