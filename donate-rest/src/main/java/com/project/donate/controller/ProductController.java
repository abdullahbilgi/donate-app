package com.project.donate.controller;


import com.project.donate.dto.ProductDocument;
import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.model.Product;
import com.project.donate.service.ProductSearchService;
import com.project.donate.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MARKET','ROLE_BENEFACTOR')")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDocument>> getAllProducts(
            @PageableDefault(size = 12, direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ResponseEntity.ok(productService.getAllProductsPageable(pageable));
    }


    @GetMapping("/donated")
    public ResponseEntity<Page<ProductDocument>> getAllDonatedProducts(
            @PageableDefault(size = 12, direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ResponseEntity.ok(productService.getDonatedProducts(pageable));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PageableDefault(size = 12, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id
    )
    {
       return ResponseEntity.ok(productService.getProductsByCategoryId(id,pageable));
    }

    @GetMapping("/city/{id}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCity(
            @PageableDefault(size = 12, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id
    )
    {
        return ResponseEntity.ok(productService.getProductsByCityId(id,pageable));
    }


    @GetMapping("/region/{id}")
    public ResponseEntity<Page<ProductResponse>> getProductsByRegion(
            @PageableDefault(size = 12, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id
    )
    {
        return ResponseEntity.ok(productService.getProductsByRegionId(id,pageable));
    }

    @GetMapping("/market/{id}")
    public ResponseEntity<Page<ProductResponse>> getProductsByMarket(
            @PageableDefault(size = 12, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id
    )
    {
        return ResponseEntity.ok(productService.getProductsByMarketId(id,pageable));
    }

    @GetMapping("/search")
    public Page<ProductDocument> searchProducts(
            @RequestParam String keyword,
            //@RequestParam(required = false) Long regionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return productService.searchProduct(keyword, PageRequest.of(page, size));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
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
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id,request));
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MARKET','ROLE_USER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
