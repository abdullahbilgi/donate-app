package com.project.donate.service;

import com.project.donate.dto.ProductDTO;
import com.project.donate.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id,ProductDTO productDTO);

    List<ProductDTO> getAllProduct();

    ProductDTO getProductById(Long id);

    ProductDTO increaseQuantity(Long id, int amount);

    ProductDTO decreaseQuantity(Long id, int amount);

    Product getProductEntityById(Long id);

    Page<ProductDTO> getAllProductsPageable(Pageable pageable);

    void deleteProduct(Long id);

    String uploadImage(MultipartFile file);

    void updateProductImage(Long productId, String imageUrl);
}

