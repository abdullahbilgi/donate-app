package com.project.donate.controller;

import com.project.donate.dto.CartDTO;
import com.project.donate.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;


    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartDTO>> getUserCarts(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getUserCartsOrderedByDate(userId));
    }

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO CartDTO) {
        return ResponseEntity.ok(cartService.createCart(CartDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long id, @RequestBody CartDTO CartDTO) {
        return ResponseEntity.ok(cartService.updateCart(id, CartDTO));
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
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
