package com.project.donate.dto.Response;

import com.project.donate.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSoldProductsResponse {
    CartProductResponse cartProductResponse;
    UserResponse userResponse;
    LocalDateTime purchaseDate;
}
