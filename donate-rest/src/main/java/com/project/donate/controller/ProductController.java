package com.project.donate.controller;

import com.project.donate.dto.ProductDTO;
import com.project.donate.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllCities() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createCity(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateCity(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id,productDTO));
    }

    @PatchMapping("/{id}/increase")
    public ResponseEntity<ProductDTO> increaseProductQuantity(
            @PathVariable Long id,
            @RequestParam int amount) {
        return ResponseEntity.ok(productService.increaseQuantity(id, amount));
    }

    @PatchMapping("/{id}/decrease")
    public ResponseEntity<ProductDTO> decreaseProductQuantity(
            @PathVariable Long id,
            @RequestParam int amount) {
        return ResponseEntity.ok(productService.decreaseQuantity(id, amount));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
