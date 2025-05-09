package com.project.donate.dto.Response;


import com.project.donate.enums.Status;
import com.project.donate.model.CartProduct;
import com.project.donate.records.ProductItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
    @Setter
    @Builder
    public class CartResponse {

        private Long id;

        private UserResponse user;

        private List<CartProductResponse> productItems;

        private Status status;

        private Double totalPrice;

        private LocalDateTime purchaseDate;

        private LocalDateTime expiredDate;

}
