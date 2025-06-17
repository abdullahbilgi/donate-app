package com.project.donate.util;

import com.google.zxing.WriterException;
import com.lowagie.text.DocumentException;
import com.project.donate.model.*;
import com.project.donate.repository.ProductRepository;
import com.project.donate.repository.UserRepository;
import com.project.donate.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(com.project.donate.config.NoSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PdfGeneratorServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PdfGeneratorService pdfGeneratorService;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);

        // Mock Authentication objesi oluştur
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test-user");

        // Mock SecurityContext oluştur ve authentication'ı set et
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mock SecurityContextHolder'ı set et
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void generateCartPdf_shouldGeneratePdfStream() throws DocumentException, WriterException {
        // Arrange
        user = new User();
        user.setName("Test");
        user.setSurname("User");
        user.setEmailVerified(true);

        userRepository.save(user);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);

        Market market = new Market();
        Address address = new Address();
        Region region = new Region();
        region.setName("RegionName");
        address.setName("Market Address");
        address.setRegion(region);
        address.setZipCode("12345");
        address.setLatitude(40.0);
        address.setLongitude(29.0);
        market.setAddress(address);
        market.setName("Test Market");
        product.setMarket(market);

        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product);
        cartProduct.setProductQuantity(1);
        cartProduct.setProductPrice(10.0);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotalPrice(10.0);
        cart.setPurchaseDate(LocalDateTime.now());
        cart.setUser(user);
        cart.setCartProducts(List.of(cartProduct));

        when(userRepository.findUserByCart(any(Cart.class))).thenReturn(Optional.of(user));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productService.getProductEntityById(any())).thenReturn(product);

        // Act
        ByteArrayInputStream result = pdfGeneratorService.generateCartPdf(cart);

        // Assert
        assertNotNull(result);
        assertTrue(result.available() > 0); // PDF content is not empty
    }
}
