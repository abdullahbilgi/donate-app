package com.project.donate.service;

import com.project.donate.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id,ProductDTO productDTO);

    List<ProductDTO> getAllProduct();

    ProductDTO getProductById(Long id);

    void deleteProduct(Long id);
}

