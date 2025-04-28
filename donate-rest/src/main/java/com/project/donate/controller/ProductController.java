package com.project.donate.controller;

import com.project.donate.dto.ProductDTO;
import com.project.donate.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ResponseEntity.ok(productService.getAllProductsPageable(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    // Resim Upload
    @PostMapping("/image/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable Long id,@RequestParam("file") MultipartFile file) {
        String imageUrl = productService.uploadImage(file);
        productService.updateProductImage(id,imageUrl);
        return ResponseEntity.ok(imageUrl);
    }


    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id,productDTO));
    }

    @PatchMapping("/{id}/increase")
    public ResponseEntity<Void> increaseProductQuantity(
            @PathVariable Long id,
            @RequestParam int amount) {
        productService.increaseQuantity(id, amount);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/decrease")
    public ResponseEntity<Void> decreaseProductQuantity(
            @PathVariable Long id,
            @RequestParam int amount) {
        productService.decreaseQuantity(id, amount);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
