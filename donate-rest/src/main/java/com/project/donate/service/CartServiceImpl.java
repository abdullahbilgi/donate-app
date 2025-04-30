package com.project.donate.service;

import com.project.donate.dto.CartDTO;
import com.project.donate.dto.Request.AddProductToCartRequest;
import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Response.CartResponse;
import com.project.donate.enums.Status;
import com.project.donate.exception.OutOfStockException;
import com.project.donate.exception.ResourceNotActiveException;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CartMapper;
import com.project.donate.model.Cart;
import com.project.donate.model.Product;
import com.project.donate.model.User;
import com.project.donate.records.ProductItem;
import com.project.donate.repository.CartRepository;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ProductService productService;


    @Override
    public List<CartResponse> getAllCarts() {

        return cartRepository.findAll()
                .stream()
                .map(cartMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartResponse getCartById(Long id) {
        log.info("{} looked cart with id: {}", GeneralUtil.extractUsername(), id);
        return cartRepository.findById(id)
                .map(cartMapper::mapToDto)
                .orElseThrow(() -> {
                    log.error("{} Cart not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Cart not found id: " + id);
                });
    }

    @Override
    public Cart getCartEntityById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} Address not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Address not found id: " + id);});
    }

    @Override
    public List<CartResponse> getUserCartsOrderedByDate(Long userId) {
        return cartRepository.findByUserIdOrderByPurchaseDateDesc(userId)
                .stream()
                .map(cartMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartResponse createCart(CartRequest request) {
        Cart cart = cartMapper.mapToEntity(request);
        cart.setStatus(Status.PENDING);
        cart.setIsActive(true);
        User user = userService.getUserEntityById(request.getUserId());
        cart.setUser(user);
        // Ürünleri kontrol et ve stoktan düş
        validateAndDecreaseStock(cart, cart.getTotalPrice());


        log.info("{} New cart created: {}", GeneralUtil.extractUsername(), cart);


        return saveAndMap(cart, "save");
    }


    @Override
    public CartResponse updateCart(Long id, CartRequest request) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} Cart not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Cart not found id: " + id);
                });

        // Eski ürünlerin stoklarını geri ver
        restoreProductQuantities(existingCart.getProductItems());

        // Yeni ürünleri kontrol et ve stoktan düş
        Cart updatedCart = cartMapper.mapToEntity(request);
        validateAndDecreaseStock(updatedCart, updatedCart.getTotalPrice());

        updatedCart.setId(id);
        updatedCart.setStatus(Status.PENDING);
        updatedCart.setIsActive(true);

        return saveAndMap(updatedCart, "update");
    }

    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartEntityById(id);
        cart.setIsActive(false);
        saveAndMap(cart, "delete");
    }

    @Override
    public CartResponse addProductToCart(AddProductToCartRequest request) {

        Cart cart = cartRepository.findByUserId(request.getUserId());
        Product product = productService.getProductEntityById(request.getProductId());
        ProductItem productItem = new ProductItem(product.getId(),product.getQuantity());
        if(cart == null) {
            cart = new Cart();
            cart.setUser(userService.getUserEntityById(request.getUserId()));
            cart.getProductItems().add(productItem);
        }else{
            cart.getProductItems().add(productItem);
        }
        validateAndDecreaseStock(cart, cart.getTotalPrice());
        return saveAndMap(cart, "save");
    }

    private void validateAndDecreaseStock(Cart cart, Double totalPrice) {
        double calculatedTotal = 0.0;
        List<ProductItem> productItems = cart.getProductItems();

        for (ProductItem item : productItems) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> {
                        log.error("{} Product not found id: {}", GeneralUtil.extractUsername(), item.productId());
                        return new ResourceNotFoundException("Product not found id: " + item.productId());
                    });

            if (!product.getIsActive()) {
                log.warn("Product not active: {}", product);
                throw new RuntimeException("Product is not active");
            }

            if (product.getQuantity() < item.quantity()) {
                log.warn("Not enough stock for product id: {} (requested: {}, available: {})",
                        item.productId(), item.quantity(), product.getQuantity());
                throw new OutOfStockException("Not enough stock for product id: " + item.productId());
            }

            product.setQuantity(product.getQuantity() - item.quantity());
            productRepository.save(product);

            calculatedTotal += product.getPrice() * item.quantity();
        }

        cart.setTotalPrice(calculatedTotal);
    }

    private void restoreProductQuantities(List<ProductItem> productItems) {
        for (ProductItem item : productItems) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> {
                        log.error("{} Product not found id: {}", GeneralUtil.extractUsername(), item.productId());
                        return new ResourceNotFoundException("Product not found while restoring: " + item.productId());
                    });

            product.setQuantity(product.getQuantity() + item.quantity());
            productRepository.save(product);
        }
    }

    @Override
    public void cancelCart(Long id) {
        Cart cart = getCartEntityById(id);
        cart.setStatus(Status.CANCELED);
        restoreProductQuantities(cart.getProductItems());
        cart.setIsActive(false);
        log.info("{} Canceled Cart with id: {}", GeneralUtil.extractUsername(), id);
        cartRepository.save(cart);


    }

    @Override
    public void approveCart(Long id) {

        Cart cart = getCartEntityById(id);
        cart.setStatus(Status.APPROVED);
        log.info("{} Approved Cart with id: {}", GeneralUtil.extractUsername(), id);
        cartRepository.save(cart);

    }


    private CartResponse saveAndMap(Cart cart, String status) {
        Cart savedCart = cartRepository.save(cart);

        switch (status) {
            case "save" -> log.info("{} Created cart: {}", GeneralUtil.extractUsername(), cart);
            case "update" -> log.info("{} Updated cart: {}", GeneralUtil.extractUsername(), cart);
            case "delete" -> log.info("{} Deleted cart: {}", GeneralUtil.extractUsername(), cart);
        }
        return cartMapper.mapToDto(savedCart);
    }
}
