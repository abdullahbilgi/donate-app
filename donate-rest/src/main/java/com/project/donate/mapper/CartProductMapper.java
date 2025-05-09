package com.project.donate.mapper;

import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.dto.Response.CartProductResponse;
import com.project.donate.dto.Response.CartResponse;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.model.CartProduct;
import com.project.donate.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartProductMapper {

    private final ProductMapper productMapper;
    //private final CartMapper cartMapper;

    public AddToCartResponse mapToDto(CartProduct cartProduct) {

        //CartResponse cartResponse = cartMapper.mapToDto(cartProduct.getCart());
        ProductResponse productResponse = productMapper.mapToDto(cartProduct.getProduct());
        return AddToCartResponse.builder()
                //.cart(cartResponse)
                .product(productResponse)
                .productQuantity(cartProduct.getProductQuantity())
                .build();
    }

    public CartProductResponse mapToCartProductResponse(CartProduct cartProduct) {
        ProductResponse product = productMapper.mapToDto(cartProduct.getProduct());
        return CartProductResponse.builder()
                .productResponse(product)
                .productQuantity(cartProduct.getProductQuantity())
                .productPrice(cartProduct.getProductPrice())
                .build();
    }
}
