package com.project.donate.mapper;

import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Request.UserDonateRequest;
import com.project.donate.dto.Response.CategoryResponse;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.dto.Response.UserDonateResponse;
import com.project.donate.enums.ProductStatus;
import com.project.donate.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

    @Component
    @RequiredArgsConstructor
    public class UserDonateMapper {
        private final CategoryMapper categoryMapper;

        public UserDonateResponse mapToDto(Product product) {
            CategoryResponse categoryResponse = categoryMapper.mapToDto(product.getCategory());
            if (product == null) return null;
            UserDonateResponse.UserDonateResponseBuilder builder = UserDonateResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .productionDate(product.getProductionDate())
                    .expiryDate(product.getExpiryDate())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .description(product.getDescription())
                    .productStatus(product.getProductStatus().toString())
                    .category(categoryResponse)
                    .imageUrl(product.getImageUrl());

            return builder.build();
        }

        public Product mapToEntity(UserDonateRequest request) {
            return Product.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .productionDate(request.getProductionDate())
                    .expiryDate(request.getExpiryDate())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .description(request.getDescription())
                    .productStatus(ProductStatus.valueOf(request.getProductStatus()))
                    .imageUrl(request.getImageUrl())
                    .build();
        }

    }