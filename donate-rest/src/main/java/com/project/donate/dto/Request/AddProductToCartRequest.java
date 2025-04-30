package com.project.donate.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddProductToCartRequest {
    @NotNull(message = "productId is mandatory")
    private Long productId;

    @NotNull(message = "quantity is mandatory")
    private Long quantity;

    private Long userId;
    // login olan userdan alınacak deneme icin böyle yazıldı
}
