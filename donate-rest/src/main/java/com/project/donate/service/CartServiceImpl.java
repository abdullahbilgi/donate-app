package com.project.donate.service;

import com.project.donate.dto.CartDTO;
import com.project.donate.dto.ProductDTO;
import com.project.donate.enums.Status;
import com.project.donate.mapper.CartMapper;
import com.project.donate.model.Cart;
import com.project.donate.model.Category;
import com.project.donate.model.Product;
import com.project.donate.records.ProductItem;
import com.project.donate.repository.CartRepository;
import com.project.donate.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;


    @Override
    public List<CartDTO> getAllCarts() {

        return cartRepository.findAll()
                .stream()
                .map(cartMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO getCartById(Long id) {
        return cartRepository.findById(id)
                .map(cartMapper::map)
                .orElseThrow(() -> new RuntimeException("Cart not found id: " + id));
    }

    @Override
    public List<CartDTO> getUserCartsOrderedByDate(Long userId) {
        return cartRepository.findByUserIdOrderByPurchaseDateDesc(userId)
                .stream()
                .map(cartMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO createCart(CartDTO cartDTO) {
        Cart cart = cartMapper.mapDto(cartDTO);

        // Ürünleri kontrol et ve stoktan düş
        validateAndDecreaseStock(cart.getProductItems(), cart.getTotalPrice());

        cart.setStatus(Status.PENDING);
        cart.setIsActive(true);

        return saveAndMap(cart);
    }


    @Override
    public CartDTO updateCart(Long id, CartDTO cartDTO) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found id: " + id));

        // Eski ürünlerin stoklarını geri ver
        restoreProductQuantities(existingCart.getProductItems());

        // Yeni ürünleri kontrol et ve stoktan düş
        Cart updatedCart = cartMapper.mapDto(cartDTO);
        validateAndDecreaseStock(updatedCart.getProductItems(), updatedCart.getTotalPrice());

        updatedCart.setId(id);
        updatedCart.setStatus(Status.PENDING);

        return saveAndMap(updatedCart);
    }

    //TODO: sepet iptal, status ayarlama, deletecart test,sepet onaylama


    @Override
    public void deleteCart(Long id) {
        CartDTO cartDTO = getCartById(id);
        Cart cart = cartMapper.mapDto(cartDTO);
        cart.setIsActive(false);
        saveAndMap(cart);
    }

    private void validateAndDecreaseStock(List<ProductItem> productItems, Double totalPrice) {
        double calculatedTotal = 0.0;

        for (ProductItem item : productItems) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found id: " + item.productId()));

            if (!product.getIsActive()) {
                throw new RuntimeException("Product is not active");
            }

            if (!product.getPrice().equals(item.productPrice())) {
                throw new RuntimeException("Product price is incorrect for product id: " + item.productId());
            }

            if (product.getQuantity() < item.quantity()) {
                throw new RuntimeException("Not enough stock for product id: " + item.productId());
            }

            product.setQuantity(product.getQuantity() - item.quantity());
            productRepository.save(product);

            calculatedTotal += item.productPrice() * item.quantity();
        }

        if (calculatedTotal != totalPrice) {
            throw new RuntimeException("Total price is incorrect");
        }
    }

    private void restoreProductQuantities(List<ProductItem> productItems) {
        for (ProductItem item : productItems) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found while restoring: " + item.productId()));

            product.setQuantity(product.getQuantity() + item.quantity());
            productRepository.save(product);
        }
    }

    @Override
    public void cancelCart(Long id) {
        CartDTO cartDTO = getCartById(id);
        Cart cart = cartMapper.mapDto(cartDTO);
        cart.setStatus(Status.CANCELED);
        restoreProductQuantities(cart.getProductItems());
        cart.setIsActive(false);
        saveAndMap(cart);

    }

    @Override
    public void approveCart(Long id) {

        CartDTO cartDTO = getCartById(id);
        Cart cart = cartMapper.mapDto(cartDTO);
        cart.setStatus(Status.APPROVED);
        saveAndMap(cart);

    }


    private CartDTO saveAndMap(Cart cart) {
        Cart savedCart = cartRepository.save(cart);

        return cartMapper.map(savedCart);
    }
}
