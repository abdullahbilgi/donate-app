package com.project.donate.dto.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartProductRequest {
    private Long productId;
    private Integer productQuantity;
    private Long userId;
    private Long cartId;
}
