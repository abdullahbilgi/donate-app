package com.project.donate.service;

import com.project.donate.dto.Request.CartProductRequest;
import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.*;
import com.project.donate.enums.Status;
import com.project.donate.exception.OutOfStockException;
import com.project.donate.exception.ResourceNotActiveException;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mail.MailMessage;
import com.project.donate.mail.MailProducer;
import com.project.donate.mapper.CartMapper;
import com.project.donate.mapper.CartProductMapper;
import com.project.donate.model.Cart;
import com.project.donate.model.CartProduct;
import com.project.donate.model.Product;
import com.project.donate.model.User;
import com.project.donate.repository.CartProductRepository;
import com.project.donate.repository.CartRepository;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import com.project.donate.util.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    private final PdfGeneratorService pdfGeneratorService;
    private final MailProducer mailProducer;
    private final CartProductMapper cartProductMapper;


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
                    return new ResourceNotFoundException("Address not found id: " + id);
                });
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
    public CartProductResponse removeProductFromCart(RemoveProductFromCartRequest request) {
        CartProduct cartProduct = cartProductService.getCartProductById(request.getCartId(), request.getProductId());
        removeProductFromCartHelper(cartProduct);
        return cartProductMapper.mapToCartProductResponse(cartProduct);
    }

    @Override
    public CartProductResponse updateProductQuantityFromCart(CartProductRequest request) {
       // Long userId = userService.getUserEntityByUsername(GeneralUtil.extractUsername()).getId();
        CartProduct cartProduct = cartProductService.getCartProductById(cartProductService.getUsersCurrentCart(request.getUserId()).getId(), request.getProductId());
        Integer fark = request.getProductQuantity() - cartProduct.getProductQuantity();
        Product product = productService.getProductEntityById(request.getProductId());
        if (product.getQuantity() - (fark) < 0) {
            throw new OutOfStockException("product sayısı yetersiz");
        } else {
            product.setQuantity(product.getQuantity() - (fark));
            cartProduct.setProductPrice(cartProduct.getProductPrice() + (fark * product.getDiscountedPrice()));
        }
        cartProduct.setProductQuantity(request.getProductQuantity());
        cartProductRepository.save(cartProduct); // cart product tablosu tamam
        Cart cart = getCartEntityById(request.getCartId());
        cart.setTotalPrice(cart.getTotalPrice() + (fark * product.getDiscountedPrice()));
        save(cart); // cart tablosu tamam

        // eger fark pozitifse eksilecek
        productService.save(product);

        return cartProductMapper.mapToCartProductResponse(cartProduct);
    }

    @Override
    public List<PurchasesProductResponse> getPurchasesProductsByUser(Long id) {
        userService.getUserEntityById(id); // Validasyon amacıyla çağrılıyor
        List<Cart> completedCarts = cartRepository.findAllByUserIdAndStatus(id, Status.APPROVED);
        return completedCarts.stream()
                .map(cartMapper::toPurchasesProductResponse)
                .toList();

    }

    public CartResponse getCurrentUserCart(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Status.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcının aktif bir sepeti bulunamadı."));
        return cartMapper.mapToDto(cart);
    }

    @Override
    public void cancelCurrentCart(Long userId) {
        Cart cart = cartProductService.getUsersCurrentCart(userId);
        List<CartProduct> cartProducts = cartProductService.getCartProducts();
        for (CartProduct cartProduct : cartProducts) {
            removeProductFromCartHelper(cartProduct);
        }
        cart.setTotalPrice(0.0);
        save(cart);
    }

    @Override
    public void removeProductFromCurrentCart(RemoveProductFromCartRequest request) {
       
    }

    @Override
    public List<CartResponse> getPurchasedProductsByUserId(Long userId) {
        List<Cart> approvedCarts = cartRepository.findAllByUserIdAndStatusOrderByPurchaseDateDesc(userId, Status.APPROVED);

        return approvedCarts.stream()
                .map(cartMapper::mapToDto)
                .toList();
    }


    @Override
    public List<GetSoldProductsResponse> getSoldProductsByUserId(Long userId) {
        List<Cart> carts = cartRepository.findApprovedCartsByProductSellerUserId(userId, Status.APPROVED);

        return carts.stream()
                .flatMap(cart -> cart.getCartProducts().stream()
                        .filter(cp -> cp.getProduct().getMarket().getUser().getId().equals(userId))
                        .map(cartProductMapper::mapToGetSoldProductsResponse)
                )
                .toList();
    }

    /**
     * private double calculateNewCartTotalPrice(List<CartProduct> cartProducts) {
     * double totalPrice  = 0;
     * for (CartProduct cartProduct : cartProducts) {
     * totalPrice+=cartProduct.getProductPrice();
     * }
     * return totalPrice;
     * }
     **/

    public void removeProductFromCartHelper(CartProduct cartProduct) {
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
        Cart cart = cartProductService.getUsersCurrentCart(request.getUserId());
        if (!product.getIsActive()) {
            log.warn("Product not active: {}", product);
            throw new ResourceNotActiveException("Product is not active");
        }
        if (product.getQuantity() < request.getProductQuantity()) {
            log.warn("Not enough stock for product id: {} (requested: {}, available: {})",
                    product.getId(), request.getProductQuantity(), product.getQuantity());
            throw new OutOfStockException("Not enough stock for product id: " + product.getId());
        }
        product.setQuantity(product.getQuantity() - request.getProductQuantity());// hocam onemli
        cart.setTotalPrice(cart.getTotalPrice() + ((product.getDiscountedPrice()) * request.getProductQuantity()));
        cartRepository.save(cart);
        productRepository.save(product);
    }

    @Scheduled(fixedRate = 60000) // her dakika çalışır
    public void restoreProductQuantities() {
        List<CartProduct> cartProducts = cartProductRepository.findAllByStatus(Status.PENDING);
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
        Cart cart = getCartEntityById(id);
        List<CartProduct> cartProducts = cartProductService.getCartProducts();
        for (CartProduct cartProduct : cartProducts) {
            removeProductFromCartHelper(cartProduct);
        }
        cart.setTotalPrice(0.0);
        save(cart);
    }

    @Override
    public void approveCart(Long id) {
        Cart cart = cartProductService.getUsersCurrentCart(id);
        List<CartProduct> cartProducts = cartProductService.getCartProducts();
        for (CartProduct cartProduct : cartProducts) {
            cartProduct.setStatus(Status.APPROVED);
            cartProductService.save(cartProduct);
        }
        cart.setStatus(Status.APPROVED);
        cart.setPurchaseDate(LocalDateTime.now());
        Cart savedCart = cartRepository.save(cart);

        // Generate PDF for mail
        ByteArrayInputStream bis = pdfGeneratorService.generateCartPdf(savedCart);

        byte[] pdfBytes;
        try {
            pdfBytes = bis.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read PDF content", e);
        }

        String subject = "Your Order Confirmation #" + cart.getId();
        //TODO: fix there "<a href="http://localhost:5173/gfhjg/%s" class="button">View Your Order</a>"
        String message = """ 
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <title>Order Confirmation</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f4f4;
                                color: #333;
                                padding: 20px;
                            }
                            .container {
                                max-width: 600px;
                                margin: auto;
                                background-color: #fff;
                                padding: 30px;
                                border-radius: 10px;
                                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                            }
                            h2 {
                                color: #2c3e50;
                            }
                            .button {
                                display: inline-block;
                                margin-top: 20px;
                                padding: 10px 20px;
                                background-color: #3498db;
                                color: white;
                                text-decoration: none;
                                border-radius: 5px;
                            }
                            .footer {
                                font-size: 12px;
                                color: #888;
                                margin-top: 30px;
                                text-align: center;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h2>Thank you for your order, %s! 🎉</h2>
                            <p>Your order has been successfully approved.</p>
                            <p>You can find your invoice attached to this email.</p>
                            <p>If you have any questions, feel free to contact our support team.</p>
                            <a href="http://localhost:5173/jvhvuh/%s" class="button">View Your Order</a>
                            <div class="footer">
                                This is an automated message, please do not reply.<br>
                                © 2025 Your Company. All rights reserved.
                            </div>
                        </div>
                    </body>
                    </html>
                """.formatted(cart.getUser().getName(), cart.getId());

        MailMessage mailMessage = new MailMessage(
                cart.getUser().getEmail(),
                subject,
                message
        );

        mailMessage.addAttachment("Order_" + cart.getId() + ".pdf", pdfBytes);

        mailProducer.sendToQueue(mailMessage);

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
