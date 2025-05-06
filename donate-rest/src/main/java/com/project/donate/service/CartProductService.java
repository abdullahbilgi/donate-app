package com.project.donate.service;

import com.project.donate.dto.Request.AddToCartRequest;
import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.model.CartProduct;
import com.project.donate.model.CartProductId;
import com.project.donate.model.Product;

import java.util.List;

public interface CartProductService {
    AddToCartResponse addProductToCart(AddToCartRequest addToCartRequest);
    void save(CartProduct cartProduct);
    void delete(CartProductId cartProductId);
    List<CartProduct> getCartProducts();
}
