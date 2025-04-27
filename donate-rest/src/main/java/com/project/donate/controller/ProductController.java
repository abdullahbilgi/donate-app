package com.project.donate.controller;

import com.project.donate.dto.ProductDTO;
import com.project.donate.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        if (image != null) {
            productDTO.setImage(image.getBytes());
        }
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
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
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
