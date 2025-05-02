package com.project.donate.mapper;

import com.project.donate.dto.ProductDTO;
import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Response.CategoryResponse;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.enums.ProductStatus;
import com.project.donate.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryMapper categoryMapper;

    public ProductResponse mapToDto(Product product) {
        CategoryResponse categoryResponse = categoryMapper.mapToDto(product.getCategory());
        if (product == null) return null;
        ProductResponse.ProductResponseBuilder builder = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .productionDate(product.getProductionDate())
                .expiryDate(product.getExpiryDate())
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .discount(product.getDiscount())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .productStatus(product.getProductStatus() != null ? product.getProductStatus().toString() : null)
                .category(categoryResponse)
                .imageUrl(product.getImageUrl());

        return builder.build();
    }

    public Product mapToEntity(ProductRequest request) {
        return Product.builder()
                .id(request.getId())
                .name(request.getName())
                .productionDate(request.getProductionDate())
                .expiryDate(request.getExpiryDate())
                .price(request.getPrice())
                .discountedPrice(request.getDiscountedPrice() != null ? request.getDiscountedPrice() : request.getPrice())
                .discount(request.getDiscount() != null ? request.getDiscount() : 0)
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .productStatus(request.getProductStatus() != null ? ProductStatus.valueOf(request.getProductStatus()) : ProductStatus.REAL)
                .imageUrl(request.getImageUrl())
                .build();
    }

}
