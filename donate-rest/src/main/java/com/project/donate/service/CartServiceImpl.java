package com.project.donate.service;

import com.project.donate.dto.Request.CartProductRequest;
import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.AddToCartResponse;
import com.project.donate.dto.Response.CartResponse;
import com.project.donate.exception.OutOfStockException;
import com.project.donate.exception.ResourceNotActiveException;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CartMapper;
import com.project.donate.model.Cart;
import com.project.donate.model.CartProduct;
import com.project.donate.model.Product;
import com.project.donate.repository.CartProductRepository;
import com.project.donate.repository.CartRepository;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {
    private final CartProductRepository cartProductRepository;

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ProductService productService;
    private final CartProductService cartProductService;


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
       /** return cartRepository.findByUserIdOrderByPurchaseDateDesc(userId)
                .stream()
                .map(cartMapper::mapToDto)
                .collect(Collectors.toList());
        **/

       return null;
    }


    @Override
    public CartResponse updateCart(Long id, CartRequest request) {
        return null;
    }

    @Override
    public void deleteCart(Long id) {
        Cart cart = getCartEntityById(id);
        cart.setIsActive(false);
        saveAndMap(cart, "delete");
    }

    @Override
    public AddToCartResponse addProductToCart(CartProductRequest request) {
        AddToCartResponse addToCartResponse = cartProductService.addProductToCart(request);
        validateAndDecreaseStock(request);
        return addToCartResponse;
    }

    @Override
    public void save(Cart cart) {
         cartRepository.save(cart);
    }

    @Override
    public Cart createCart() {
        Cart cart = new Cart();
        double initTotalPrice = 0;
        cart.setTotalPrice(initTotalPrice);
        save(cart);
        return cart;
    }

    @Override
    public void removeProductFromCart(RemoveProductFromCartRequest request) {
        CartProduct cartProduct = cartProductService.getCartProductById(request.getCartId(),request.getProductId());
        removeProductFromCartHelper(cartProduct);
    }

    @Override
    public void updateProductQuantityFromCart(CartProductRequest request) {
        CartProduct cartProduct = cartProductService.getCartProductById(request.getCartId(),request.getProductId());
        Integer fark = request.getProductQuantity()-cartProduct.getProductQuantity();
        Product product = productService.getProductEntityById(request.getProductId());
        if(product.getQuantity()-(fark) < 0)
        {
            throw new  OutOfStockException("product sayısı yetersiz");
        }else{
            product.setQuantity(product.getQuantity()-(fark));
        }
        cartProduct.setProductQuantity(request.getProductQuantity());
        cartProductRepository.save(cartProduct); // cart product tablosu tamam
        Cart cart = getCartEntityById(request.getCartId());
        cart.setTotalPrice(cart.getTotalPrice() + (fark*product.getDiscountedPrice()));
        save(cart); // cart tablosu tamam

        // eger fark pozitifse eksilecek
        productService.save(product);
    }

    public void removeProductFromCartHelper(CartProduct cartProduct){
        Product product = cartProduct.getProduct();
        product.setQuantity(product.getQuantity() + cartProduct.getProductQuantity());
        productService.save(product);
        Cart cart = cartProduct.getCart();
        cart.setTotalPrice(cart.getTotalPrice() - (product.getDiscountedPrice() * cartProduct.getProductQuantity()));
        save(cart);
        cartProductService.delete(cartProduct.getId());
    }

    private void validateAndDecreaseStock(CartProductRequest request) {

        Product product = productService.getProductEntityById(request.getProductId());
        Cart cart = getCartEntityById(request.getCartId());
        if(!product.getIsActive()){
            log.warn("Product not active: {}", product);
            throw new ResourceNotActiveException("Product is not active");
        }
        if (product.getQuantity() < request.getProductQuantity()) {
            log.warn("Not enough stock for product id: {} (requested: {}, available: {})",
                    product.getId(), request.getProductQuantity(), product.getQuantity());
            throw new OutOfStockException("Not enough stock for product id: " + product.getId());
        }
        product.setQuantity(product.getQuantity()-request.getProductQuantity());// hocam onemli
        cart.setTotalPrice(cart.getTotalPrice() + ((product.getDiscountedPrice())* request.getProductQuantity()));
        cartRepository.save(cart);
        productRepository.save(product);
    }

    @Scheduled(fixedRate = 60000) // her dakika çalışır
    public void restoreProductQuantities() {
        List<CartProduct> cartProducts = cartProductService.getCartProducts();
        LocalDateTime now = LocalDateTime.now();

        for (CartProduct cartProduct : cartProducts) {
            LocalDateTime addedTime = cartProduct.getProductAddedTime();

            if (addedTime != null && addedTime.isBefore(now.minusMinutes(15))) {
                removeProductFromCartHelper(cartProduct);
            }
        }
    }

    @Override
    public void cancelCart(Long id) {
       /** Cart cart = getCartEntityById(id);
        cart.setStatus(Status.CANCELED);
        restoreProductQuantities(cart.getProductItems());
        cart.setIsActive(false);
        log.info("{} Canceled Cart with id: {}", GeneralUtil.extractUsername(), id);
        cartRepository.save(cart);
        **/
    }

    @Override
    public void approveCart(Long id) {
        /**

        Cart cart = getCartEntityById(id);
        cart.setStatus(Status.APPROVED);
        log.info("{} Approved Cart with id: {}", GeneralUtil.extractUsername(), id);
        cartRepository.save(cart);
         **/

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
