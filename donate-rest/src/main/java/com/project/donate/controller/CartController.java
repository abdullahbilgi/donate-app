package com.project.donate.controller;

import com.project.donate.dto.Request.CartProductRequest;
import com.project.donate.dto.Request.CartRequest;
import com.project.donate.dto.Request.RemoveProductFromCartRequest;
import com.project.donate.dto.Response.*;
import com.project.donate.model.Cart;
import com.project.donate.service.CartService;
import com.project.donate.util.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MARKET','ROLE_BENEFACTOR')")
public class CartController {

    private final CartService cartService;
    private final PdfGeneratorService pdfGeneratorService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<CartResponse>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/purchasedProducts/{userId}")
    public ResponseEntity<List<CartResponse>> getPurchasedProductsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getPurchasedProductsByUserId(userId));
    }

    @GetMapping("/soldProducts/{userId}")
    public ResponseEntity<List<GetSoldProductsResponse>> getSoldProductsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getSoldProductsByUserId(userId));
    }


    @GetMapping("/currentUserCart/{userId}")
    public ResponseEntity<CartResponse> getCurrentUserCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCurrentUserCart(userId));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getCartPdf(@PathVariable Long id) {
        Cart cart = cartService.getCartEntityById(id);
        ByteArrayInputStream bis = pdfGeneratorService.generateCartPdf(cart);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=order_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }


    /**@PostMapping
    public ResponseEntity<CartResponse> createCart(@RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.createCart(request));
    }**/

    @PostMapping("/addProduct")
    public ResponseEntity<AddToCartResponse> addProductToCart(@RequestBody CartProductRequest request) {
        return ResponseEntity.ok(cartService.addProductToCart(request));
    }

    @PostMapping("/removeProduct")
    public ResponseEntity<CartProductResponse> removeProductToCart(@RequestBody RemoveProductFromCartRequest request) {
    CartProductResponse cartProductResponse = cartService.removeProductFromCart(request);
        return ResponseEntity.ok(cartProductResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long id, @RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.updateCart(id, request));
    }

    @PutMapping("/updateCartProduct")
    public ResponseEntity<CartProductResponse> updateProductCart(@RequestBody CartProductRequest request) {
    CartProductResponse cartProductResponse = cartService.updateProductQuantityFromCart(request);
       return ResponseEntity.ok(cartProductResponse);
    }

    @PutMapping("/cancelCart/{id}")
    public ResponseEntity<Void> cancelCart(@PathVariable Long id) {
        cartService.cancelCart(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/approveCart/{id}")
    public ResponseEntity<Void> approveCart(@PathVariable Long id) {
        cartService.approveCart(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    /**
    @GetMapping("/purchasedProducts/{id}")
    public ResponseEntity<List<PurchasesProductResponse>> getPurchasesProductByUser(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getPurchasesProductsByUser(id));
    }
    **/
}
