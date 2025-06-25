package com.project.donate.mapper;

import com.project.donate.dto.Response.*;
import com.project.donate.model.CartProduct;
import com.project.donate.model.Product;
import com.project.donate.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CartProductMapper {

    private final ProductMapper productMapper;
    //private final CartMapper cartMapper;
    private final UserMapper userMapper;

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
                .cartId(cartProduct.getId().getCartId())
                .build();
    }

    public GetSoldProductsResponse mapToGetSoldProductsResponse(CartProduct cartProduct) {
        LocalDateTime purchaseDateTime = cartProduct.getCart().getPurchaseDate();
        User user = cartProduct.getCart().getUser();
        UserResponse userResponse = userMapper.userToUserDto(user);
        CartProductResponse cartProductResponse = mapToCartProductResponse(cartProduct);
        return GetSoldProductsResponse.builder()
                .cartProductResponse(cartProductResponse)
                .purchaseDate(purchaseDateTime)
                .userResponse(userResponse)
                .build();
    }

}
