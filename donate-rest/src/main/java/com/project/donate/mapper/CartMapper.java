package com.project.donate.mapper;


import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Response.*;
import com.project.donate.model.Cart;
import com.project.donate.model.CartProduct;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartMapper  {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final CartProductMapper cartProductMapper;

    public CartResponse mapToDto(Cart cart) {
        List<CartProductResponse> productResponses = cart.getCartProducts()
                .stream()
                .map(cartProductMapper::mapToCartProductResponse)
                .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .productItems(productResponses)
                .status(cart.getStatus())
                .totalPrice(cart.getTotalPrice())
                .purchaseDate(cart.getPurchaseDate())
                .build();
    }

    public Cart mapToEntity(CartRequest request) {

        return Cart.builder()
                //.id(request.getId())
                .build();
    }


    public CartProductResponse toCartProductResponse(CartProduct cartProduct) {
        CartProductResponse response = new CartProductResponse();

        ProductResponse productResponse = productMapper.mapToDto(cartProduct.getProduct());

        response.setProductResponse(productResponse);
        response.setProductPrice(cartProduct.getProductPrice());
        response.setProductQuantity(cartProduct.getProductQuantity());

        return response;
    }


    public PurchasesProductResponse toPurchasesProductResponse(Cart cart) {
        PurchasesProductResponse response = new PurchasesProductResponse();
        response.setPurchaseDate(cart.getPurchaseDate());
        response.setTotalPrice(cart.getTotalPrice());

        List<CartProductResponse> productResponses = cart.getCartProducts().stream()
                .map(this::toCartProductResponse)
                .toList();

        response.setCartProductResponseList(productResponses);
        return response;
    }
}
