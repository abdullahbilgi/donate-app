package com.project.donate.mapper;

import com.project.donate.dto.CartDTO;
import com.project.donate.model.Cart;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper implements ObjectMapper<Cart, CartDTO> {

    private final UserRepository userRepository;

    @Override
    public CartDTO map(Cart cart) {
        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .productItems(cart.getProductItems())
                .status(cart.getStatus())
                .totalPrice(cart.getTotalPrice())
                .purchaseDate(cart.getPurchaseDate())
                .isActive(cart.getIsActive())
                .expiredDate(cart.getExpiredDate())
                .build();
    }

    @Override
    public Cart mapDto(CartDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));

        return Cart.builder()
                .id(dto.getId())
                .user(user)
                .productItems(dto.getProductItems())
                .status(dto.getStatus())
                .totalPrice(dto.getTotalPrice())
                .isActive(dto.getIsActive())
                .build();
    }
}
