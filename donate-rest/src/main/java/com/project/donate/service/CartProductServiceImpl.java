package com.project.donate.service;

import com.project.donate.dto.Request.AddToCartRequest;
import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CartProductMapper;
import com.project.donate.model.Cart;
import com.project.donate.model.CartProduct;
import com.project.donate.model.CartProductId;
import com.project.donate.model.Product;
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

    @Override
    public AddToCartResponse addProductToCart(AddToCartRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartProductId cartProductId = new CartProductId(cart.getId(), product.getId());

        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(cartProductId);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setProductQuantity(request.getProductQuantity());
        cartProduct.setProductAddedTime(LocalDateTime.now());

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
}
