package com.project.donate.dto.Response;

import com.project.donate.model.Product;
import lombok.Data;

@Data
public class CartProductResponse {
    private ProductResponse productResponse;
    private Double productPrice;
    private Integer productQuantity;
}
