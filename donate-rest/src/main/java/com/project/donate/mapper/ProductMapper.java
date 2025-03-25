package com.project.donate.mapper;

import com.project.donate.dto.ProductDTO;
import com.project.donate.enums.ProductStatus;
import com.project.donate.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements ObjectMapper<Product, ProductDTO> {

    @Override
    public ProductDTO map(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .productionDate(product.getProductionDate())
                .expiryDate(product.getExpiryDate())
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .discount(product.getDiscount())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .productStatus(product.getProductStatus().toString())
                .isActive(product.getIsActive())
                .category(product.getCategory())
                .build();
    }

    @Override
    public Product mapDto(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .productionDate(productDTO.getProductionDate())
                .expiryDate(productDTO.getExpiryDate())
                .price(productDTO.getPrice())
                .discountedPrice(productDTO.getDiscountedPrice() != null ? productDTO.getDiscountedPrice() : productDTO.getPrice())
                .discount(productDTO.getDiscount() != null ? productDTO.getDiscount() : 0)
                .quantity(productDTO.getQuantity())
                .description(productDTO.getDescription())
                .productStatus(productDTO.getProductStatus() != null ? ProductStatus.valueOf(productDTO.getProductStatus()) : ProductStatus.REAL)
                .category(productDTO.getCategory())
                .build();
    }
}
