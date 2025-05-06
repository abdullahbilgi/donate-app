package com.project.donate.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class AddToCartRequest {
    private Long cartId;
    private Long productId;
    private Integer productQuantity;
}
