package com.project.donate.register;

import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.dto.Response.AddressResponse;
import com.project.donate.mapper.AddressMapper;
import com.project.donate.model.Address;
import com.project.donate.model.Cart;
import com.project.donate.model.Region;
import com.project.donate.model.User;
import com.project.donate.repository.AddressRepository;
import com.project.donate.repository.UserRepository;
import com.project.donate.service.AddressService;
import com.project.donate.service.CartService;
import com.project.donate.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final CartService cartService;

    public void register(UserRegistrationRequest request) {

        Address address = addressService.createAddressEntity(request.getAddress());
        addressService.saveAddress(address);
        Cart cart = cartService.createCart();
        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUserName())
                .email(request.getMail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .age(request.getAge())
                .role(request.getRole())
                .address(address)
                .cart(cart)
                .build();
        userRepository.save(user);

        log.info("User registered - {}", user.getUsername() );
    }
}
