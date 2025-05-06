package com.project.donate.service;

import com.project.donate.dto.Request.CartProductResponse;
import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.dto.Response.CartResponse;
import com.project.donate.model.Cart;

import java.util.List;

public interface CartService {


    CartResponse updateCart(Long id, CartRequest request);

    List<CartResponse> getAllCarts();

    CartResponse getCartById(Long id);

    Cart getCartEntityById(Long id);

    List<CartResponse> getUserCartsOrderedByDate(Long userId);

    void cancelCart(Long id);

    void approveCart(Long id);

    void deleteCart(Long id);

    AddToCartResponse addProductToCart(CartProductResponse request);

    void save(Cart cart);

    Cart createCart();

    void removeProductFromCart(RemoveProductFromCartRequest request);

}
