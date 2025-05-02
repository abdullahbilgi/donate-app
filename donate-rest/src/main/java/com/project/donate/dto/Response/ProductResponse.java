package com.project.donate.dto.Response;

import com.project.donate.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    private Double price;

    private Double discountedPrice;

    private Integer discount;

    private Integer quantity;

    private String description;

    private String productStatus;

    private CategoryResponse category;

    private String imageUrl;

    private CategoryResponse categoryResponse;
}
