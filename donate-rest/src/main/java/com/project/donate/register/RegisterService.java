package com.project.donate.register;

import com.project.donate.mapper.AddressMapper;
import com.project.donate.model.Address;
import com.project.donate.model.User;
import com.project.donate.repository.AddressRepository;
import com.project.donate.repository.UserRepository;
import com.project.donate.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public void register(UserRegistrationRequest request) {

        Address address =  addressRepository.save(addressMapper.mapDto(request.getAddress()));
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
        userRepository.save(user);
    }
}
