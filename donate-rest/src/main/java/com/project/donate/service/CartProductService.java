package com.project.donate.service;

import com.project.donate.dto.Request.CartProductResponse;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.model.CartProduct;
import com.project.donate.model.CartProductId;

import java.util.List;

public interface CartProductService {
    AddToCartResponse addProductToCart(CartProductResponse cartProductResponse);
    void save(CartProduct cartProduct);
    void delete(CartProductId cartProductId);
    List<CartProduct> getCartProducts();
    void removeProductFromCart(RemoveProductFromCartRequest request);
    CartProduct getCartProductById(Long cartId,Long productId);
}
