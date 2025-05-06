package com.project.donate.dto.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveProductFromCartRequest {
    private Long cartId;
    private Long productId;
}
