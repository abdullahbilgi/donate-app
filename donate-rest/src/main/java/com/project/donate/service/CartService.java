package com.project.donate.service;

import com.project.donate.dto.Request.CartProductRequest;
import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.*;
import com.project.donate.model.Cart;

import java.util.List;

public interface CartService {


    CartResponse updateCart(Long id, CartRequest request);

    List<CartResponse> getAllCarts();

    CartResponse getCartById(Long id);

    Cart getCartEntityById(Long id);

    List<CartResponse> getUserCartsOrderedByDate(Long userId);

    void cancelCart(Long id);

    CartResponse approveCart(Long id);

    void deleteCart(Long id);

    AddToCartResponse addProductToCart(CartProductRequest request);

    void save(Cart cart);

    Cart createCart();

    CartProductResponse removeProductFromCart(RemoveProductFromCartRequest request);

    CartProductResponse updateProductQuantityFromCart(CartProductRequest request);

    List<PurchasesProductResponse> getPurchasesProductsByUser(Long id);

    CartResponse getCurrentUserCart(Long userId);

    void cancelCurrentCart(Long userId);

    void removeProductFromCurrentCart(RemoveProductFromCartRequest request);

    List<CartResponse> getPurchasedProductsByUserId(Long userId);

    List<GetSoldProductsResponse> getSoldProductsByMarketId(Long userId);




}
