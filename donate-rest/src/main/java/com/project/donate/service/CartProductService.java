package com.project.donate.service;

import com.project.donate.dto.Request.CartProductRequest;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.model.CartProduct;
import com.project.donate.model.CartProductId;

import java.util.List;

public interface CartProductService {
    AddToCartResponse addProductToCart(CartProductRequest cartProductRequest);
    void save(CartProduct cartProduct);
    void delete(CartProductId cartProductId);
    List<CartProduct> getCartProducts();
    void removeProductFromCart(RemoveProductFromCartRequest request);
    CartProduct getCartProductById(Long cartId,Long productId);
}
