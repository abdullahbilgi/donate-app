package com.project.donate.service;

import com.project.donate.dto.ProductDocument;
import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id,ProductRequest request);

    List<ProductResponse> getAllProduct();

    ProductResponse getProductById(Long id);

    void increaseQuantity(Long id, int amount);

    void decreaseQuantity(Long id, int amount);

    Product getProductEntityById(Long id);

    Page<ProductDocument> getAllProductsPageable(Pageable pageable);

    void deleteProduct(Long id);

    String uploadImage(MultipartFile file);

    void updateProductImage(Long productId, String imageUrl);

    void save(Product product);

    Page<ProductResponse> getProductsByCategoryId(Long id, Pageable pageable);

    Page<ProductResponse> getProductsByCityId(Long id, Pageable pageable);

    Page<ProductResponse> getProductsByRegionId(Long id, Pageable pageable);

    Page<ProductResponse> getProductsByMarketId(Long id, Pageable pageable);

    Page<ProductDocument> searchProduct(String keyword, Pageable pageable);

    Page<ProductDocument> getDonatedProducts(Pageable pageable);


}

