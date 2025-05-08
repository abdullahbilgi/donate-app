package com.project.donate.service;

import com.project.donate.dto.Request.CartProductRequest;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.enums.Status;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CartProductMapper;
import com.project.donate.model.*;
import com.project.donate.repository.CartProductRepository;
import com.project.donate.repository.CartRepository;
import com.project.donate.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartProductServiceImpl implements CartProductService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final CartProductMapper cartProductMapper;
    private final UserService userService;

    @Override
    public AddToCartResponse addProductToCart(CartProductRequest request) {
        Cart cart = getUsersCurrentCart(request.getUserId());

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartProductId cartProductId = new CartProductId(cart.getId(), product.getId());

        CartProduct cartProduct;

        if (cartProductRepository.existsByIdCartIdAndIdProductIdAndStatus(cart.getId(), request.getProductId(),Status.PENDING)) {
            // Ürün zaten sepette, var olanı güncelle
            cartProduct = cartProductRepository.findById(cartProductId)
                    .orElseThrow(() -> new RuntimeException("CartProduct not found despite existence check"));

            cartProduct.setProductQuantity(request.getProductQuantity() + cartProduct.getProductQuantity()); // ya da üstüne ekle: +=
            cartProduct.setProductPrice(cartProduct.getProductPrice() +request.getProductQuantity() * product.getDiscountedPrice());
            cartProduct.setProductAddedTime(LocalDateTime.now());//todo degistirlebilir
        } else {
            // Yeni CartProduct oluştur
            cartProduct = new CartProduct();
            cartProduct.setId(cartProductId);
            cartProduct.setCart(cart);
            cartProduct.setProduct(product);
            cartProduct.setProductQuantity(request.getProductQuantity());
            cartProduct.setProductPrice(request.getProductQuantity() * product.getDiscountedPrice());
            cartProduct.setProductAddedTime(LocalDateTime.now());
            cartProduct.setStatus(Status.PENDING);
        }

        cartProductRepository.save(cartProduct);
        return cartProductMapper.mapToDto(cartProduct);
    }



    @Override
    public void save(CartProduct cartProduct) {
        cartProductRepository.save(cartProduct);
    }

    @Override
    public void delete(CartProductId cartProductId) {
        cartProductRepository.deleteById(cartProductId);
    }

    @Override
    public List<CartProduct> getCartProducts() {
        return cartProductRepository.findAll();
    }

    @Override
    public void removeProductFromCart(RemoveProductFromCartRequest request) {
        CartProduct cartProduct = cartProductRepository
                .findByIdCartIdAndIdProductIdAndStatus(request.getCartId(),request.getProductId(),Status.PENDING);
        cartProductRepository.delete(cartProduct);
    }

    @Override
    public CartProduct getCartProductById(Long cartId, Long productId) {
        return cartProductRepository.findByIdCartIdAndIdProductIdAndStatus(cartId, productId,Status.PENDING);
    }

    @Override
    public Cart getUsersCurrentCart(long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId,Status.PENDING);
        User user = userService.getUserEntityById(userId);
        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart.setStatus(Status.PENDING);
            cart.setTotalPrice(0.0);
            cartRepository.save(cart);
        }
        return cart;
    }
}
