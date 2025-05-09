package com.project.donate.mapper;

import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Response.CategoryResponse;
import com.project.donate.dto.Response.MarketResponse;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.model.Market;
import com.project.donate.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final CategoryMapper categoryMapper;


    public ProductResponse mapToDto(Product product) {
        if (product == null) return null;

        CategoryResponse categoryResponse = categoryMapper.mapToDto(product.getCategory());

        Market market = product.getMarket();
        MarketResponse marketResponse = MarketResponse.builder()
                .id(market.getId())
                .name(market.getName())
                .taxNumber(market.getTaxNumber())
                .build();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .productionDate(product.getProductionDate())
                .expiryDate(product.getExpiryDate())
                .lastDonatedDate(product.getLastDonatedDate())
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .discount(product.getDiscount())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .productStatus(product.getProductStatus() != null ? product.getProductStatus().toString() : null)
                .category(categoryResponse)
                .market(marketResponse)
                .imageUrl(product.getImageUrl())
                .build();
    }

    public Product mapToEntity(ProductRequest request) {
        return Product.builder()
                .id(request.getId())
                .name(request.getName())
                .productionDate(request.getProductionDate())
                .expiryDate(request.getExpiryDate())
                .lastDonatedDate(request.getLastDonatedDate())
                .price(request.getPrice())
                .discountedPrice(request.getDiscountedPrice() != null ? request.getDiscountedPrice() : request.getPrice())
                .productStatus(request.getProductStatus() != null ? request.getProductStatus() : null)
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .build();
    }

}
