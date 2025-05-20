package com.project.donate.register;

import com.project.donate.mail.VerificationService;
import com.project.donate.model.Address;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import com.project.donate.service.AddressService;
import com.project.donate.service.CartService;
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
    private final VerificationService verificationService;

    public void register(UserRegistrationRequest request) {

        Address address = addressService.createAddressEntity(request.getAddress());
        addressService.saveAddress(address);
        //Cart cart = cartService.createCart();
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
                .build();
        User userForVerify =userRepository.save(user);

        verificationService.sendVerificationEmail(userForVerify);


        log.info("User registered - {}", user.getUsername() );
    }
}
