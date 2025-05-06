package com.project.donate.mapper;

import com.project.donate.dto.CartDTO;
import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Response.CartResponse;
import com.project.donate.dto.Response.UserResponse;
import com.project.donate.model.Cart;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper  {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CartResponse mapToDto(Cart cart) {
        //UserResponse user = userMapper.userToUserDto(cart.getUser());
        return CartResponse.builder()
                .id(cart.getId())
                //.user(user)
                //.productItems(cart.getProductItems())
                .status(cart.getStatus())
                .totalPrice(cart.getTotalPrice())
                .purchaseDate(cart.getPurchaseDate())
                .expiredDate(cart.getExpiredDate())
                .build();
    }

    public Cart mapToEntity(CartRequest request) {

        return Cart.builder()
                //.id(request.getId())
                .build();
    }
}
