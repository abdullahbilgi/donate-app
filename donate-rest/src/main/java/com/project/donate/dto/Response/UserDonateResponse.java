package com.project.donate.dto.Response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDonateResponse {
    private Long id;

    private String name;

    private LocalDateTime productionDate;

    private LocalDateTime expiryDate;

    private Double price;

    private Integer quantity;

    private String description;

    private String productStatus;

    private CategoryResponse category;

    private String imageUrl;

}