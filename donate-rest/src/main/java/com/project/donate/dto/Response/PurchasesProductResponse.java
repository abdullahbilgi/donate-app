package com.project.donate.dto.Response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchasesProductResponse {

    private LocalDateTime purchaseDate;
    private Double totalPrice;
    private List<CartProductResponse> cartProductResponseList;
}
