package com.project.donate.repository;

import com.project.donate.enums.Role;
import com.project.donate.enums.Status;
import com.project.donate.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MarketRepositoryTest {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User user;
    private Address address;

    @BeforeEach
    public void setUp() {
        City city = new City();
        city.setName("Example City");
        city = cityRepository.save(city);

        Region region = new Region();
        region.setName("Example Region");
        region.setCity(city);
        region = regionRepository.save(region);


        address = new Address();
        address.setName("Example Address");
        address.setRegion(region);
        address = addressRepository.save(address);


        user = User.builder()
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

        Market activeMarket = Market.builder()
                .name("Market1")
                .status(Status.APPROVED)
                .isActive(true)
                .user(user)
                .address(address)
                .build();

        Market passiveMarket = Market.builder()
                .name("Market2")
                .status(Status.PENDING)
                .isActive(true)
                .user(user)
                .address(address)
                .build();

        Market deletedMarket = Market.builder()
                .name("Market3")
                .status(Status.REJECTED)
                .isActive(false)
                .user(user)
                .address(address)
                .build();

        marketRepository.saveAll(List.of(activeMarket, passiveMarket, deletedMarket));
    }

    @Test
    void testFindByIsActiveTrue() {
        List<Market> activeMarkets = marketRepository.findByIsActiveTrue();

        assertThat(activeMarkets).hasSize(2);
        assertThat(activeMarkets).allMatch(m -> m.getIsActive().equals(true));
    }

    @Test
    void testGetMarketsByStatusAndIsActiveTrue() {
        List<Market> activeStatusMarkets = marketRepository.getMarketsByStatusAndIsActiveTrue(Status.APPROVED);

        assertThat(activeStatusMarkets).hasSize(1);
        assertThat(activeStatusMarkets.get(0).getStatus()).isEqualTo(Status.APPROVED);
    }

    @Test
    void testFindAllByUserIdAndIsActiveTrue() {
        List<Market> userMarkets = marketRepository.findAllByUserIdAndIsActiveTrue(user.getId());

        assertThat(userMarkets).hasSize(2);
        assertThat(userMarkets).allMatch(m -> m.getUser().getId().equals(user.getId()) && m.getIsActive());
    }
}
