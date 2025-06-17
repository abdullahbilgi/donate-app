package com.project.donate.register;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.donate.Jwt.JWTAuthenticationFilter;
import com.project.donate.Jwt.JWTService;
import com.project.donate.dto.Request.AddressRequest;
import com.project.donate.enums.Role;
import com.project.donate.model.City;
import com.project.donate.model.Region;
import com.project.donate.repository.CityRepository;
import com.project.donate.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestConfiguration.class)
@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterService registerService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private RegionRepository regionRepository;

    @MockBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private UserRegistrationRequest request;

    @BeforeEach
    void setUp() {

        City city = new City();
        city.setName("Example City");
        city = cityRepository.save(city);

        Region region = new Region();
        region.setName("Example Region");
        region.setCity(city);
        regionRepository.save(region);

        AddressRequest addressRequest = AddressRequest.builder()
                .name("testAddress")
                .latitude(1.0)
                .zipCode("12345")
                .longitude(2.0)
                .regionId(1L)
                .build();

        request = new UserRegistrationRequest();
        request.setName("Ali");
        request.setSurname("Veli");
        request.setUserName("aliveli");
        request.setMail("ali@example.com");
        request.setPassword("TestPass123");
        request.setPhone("5555555555");
        request.setAge(30);
        request.setRole(Role.USER);
        request.setAddress(addressRequest);

        Mockito.doNothing().when(registerService).register(Mockito.any(UserRegistrationRequest.class));
    }

    @Test
    @WithMockUser
    void registerUser_shouldReturnOkAndCallService() throws Exception {
        mockMvc.perform(post("/api/v1/register/register-user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));

        Mockito.verify(registerService, times(1)).register(Mockito.any(UserRegistrationRequest.class));
    }


}
