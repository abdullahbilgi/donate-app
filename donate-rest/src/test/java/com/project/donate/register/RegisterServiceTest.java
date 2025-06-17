package com.project.donate.register;

import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.enums.Role;
import com.project.donate.mail.VerificationService;
import com.project.donate.model.Address;
import com.project.donate.model.City;
import com.project.donate.model.Region;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import com.project.donate.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AddressService addressService;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private RegisterService registerService;

    private Region region;

    @BeforeEach
    void setUp() {

        City city = new City();
        city.setName("Example City");


        region = new Region();
        region.setName("Example Region");
        region.setCity(city);


        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_ShouldCreateAndSaveUserAndSendVerificationEmail() {
        AddressRequest addressRequest = AddressRequest.builder()
                .name("testAddress")
                .latitude(1.0)
                .zipCode("12345")
                .longitude(2.0)
                .build();

        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Ali");
        request.setSurname("Veli");
        request.setUserName("aliveli");
        request.setMail("ali@example.com");
        request.setPassword("123456");
        request.setPhone("5555555555");
        request.setAge(30);
        request.setRole(Role.USER);
        request.setAddress(addressRequest);


        Address savedAddress = Address.builder()
                .name("testAddress")
                .latitude(1.0)
                .longitude(2.0)
                .zipCode("12345")
                .region(region)
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username("aliveli")
                .build();

        when(addressService.createAddressEntity(any())).thenReturn(savedAddress);
        when(addressService.saveAddress(savedAddress)).thenReturn(savedAddress);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        registerService.register(request);

        // Assert
        verify(addressService).createAddressEntity(request.getAddress());
        verify(addressService).saveAddress(savedAddress);
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));
        verify(verificationService).sendVerificationEmail(savedUser);
    }
}
