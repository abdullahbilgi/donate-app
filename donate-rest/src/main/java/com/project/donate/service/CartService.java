package com.project.donate.service;

import com.project.donate.dto.CartDTO;

import java.util.List;

public interface CartService {

    CartDTO createCart(CartDTO cartDTO);

    CartDTO updateCart(Long id, CartDTO cartDTO);

    List<CartDTO> getAllCarts();

    CartDTO getCartById(Long id);

    List<CartDTO> getUserCartsOrderedByDate(Long userId);

    void cancelCart(Long id);

    void approveCart(Long id);

    void deleteCart(Long id);
}
