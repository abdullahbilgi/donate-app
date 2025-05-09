package com.project.donate.dto.Response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private LocalDateTime productionDate;

    private LocalDateTime expiryDate;

    private LocalDateTime lastDonatedDate;

    private Double price;

    private Double discountedPrice;

    private Integer discount;

    private Integer quantity;

    private String description;

    private String productStatus;

    private CategoryResponse category;

    private MarketResponse market;

    private String imageUrl;

}
