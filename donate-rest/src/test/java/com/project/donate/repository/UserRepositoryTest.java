package com.project.donate.repository;

import com.project.donate.enums.Role;
import com.project.donate.enums.Status;
import com.project.donate.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void beforeEach() {
        City city = new City();
        city.setName("Example City");
        city = cityRepository.save(city);

        Region region = new Region();
        region.setName("Example Region");
        region.setCity(city);
        region = regionRepository.save(region);


        Address address = new Address();
        address.setName("Example Address");
        address.setRegion(region);
        address = addressRepository.save(address);


        User user = User.builder()
                .name("Ali")
                .surname("Veli")
                .username("ali123")
                .password("pass123")
                .email("ali@example.com")
                .phone("1234567890")
                .age(30)
                .role(Role.USER)
                .emailVerified(true)
                .address(address)
                .build();

        user = userRepository.save(user);

        Cart cart = Cart.builder()
                .user(user)
                .totalPrice(100.0)
                .status(Status.APPROVED)
                .build();
        cartRepository.save(cart);
    }


    @Test
    void testFindUserByUsername() {

        Optional<User> found = userRepository.findUserByUsername("ali123");

        assertTrue(found.isPresent());
        assertEquals("ali@example.com", found.get().getEmail());
    }

    @Test
    void testFindUserByEmail() {

        User found = userRepository.findByEmail("ali@example.com");

        assertNotNull(found);
        assertEquals("ali123", found.getUsername());
    }

    @Test
    void testFindUserByCart() {
        User user = userRepository.findByEmail("ali@example.com");
        assertNotNull(user);

        Cart cart = cartRepository.findAll().stream()
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        Optional<User> foundUser = userRepository.findUserByCart(cart);

        assertTrue(foundUser.isPresent());
        assertEquals("ali123", foundUser.get().getUsername());
        assertEquals("ali@example.com", foundUser.get().getEmail());
    }

}
