package com.project.donate.service;


import com.project.donate.dto.ProductDTO;
import com.project.donate.enums.ProductStatus;
import com.project.donate.mapper.ProductMapper;
import com.project.donate.model.Product;
import com.project.donate.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public List<ProductDTO> getAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::map)
                .orElseThrow(() -> new RuntimeException("Product not found id: " + id));
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.mapDto(productDTO);
        calculateDiscountPrice(product);
        return saveAndMap(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        getProductById(id);
        Product savingProduct = productMapper.mapDto(productDTO);
        savingProduct.setId(id);
        calculateDiscountPrice(savingProduct);

        return saveAndMap(savingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductDTO productDTO = getProductById(id);
        Product product = productMapper.mapDto(productDTO);
        product.setIsActive(false);
        saveAndMap(product);

    }

    /**
     * Her gün gece yarısı (00:00:00) çalışarak tüm ürünleri günceller.
     */
    @Scheduled(cron = "0 0 0 * * ?") // Her gün 00:00:00'da çalışır
    public void updateAllProductDiscounts() {
        List<Product> products = productRepository.findAllByIsActiveTrue();
        for (Product product : products) {
            calculateDiscountPrice(product);
            productRepository.save(product);
        }
        //todo: use log
        System.out.println("Updated active products! [" + LocalDateTime.now() + "]");
    }

    private void calculateDiscountPrice(Product product) {
        if (product.getExpiryDate() == null || product.getPrice() == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, product.getExpiryDate());
        long daysRemaining = duration.toDays();

        if (daysRemaining > 150) { // 5 aydan fazla süresi var, indirim yok
            product.setDiscountedPrice(product.getPrice());

        } else if (daysRemaining > 60) { // 2-5 ay arası, kademeli indirim
            product.setProductStatus(ProductStatus.DISCOUNT);

            int discountPercentage = (int) ((150 - daysRemaining) * 100 / 90); // Gün sayısına göre dinamik indirim (max %100)
            discountPercentage = Math.min(discountPercentage, 80); // Maksimum %80 indirim uygulanır
            product.setDiscount(discountPercentage);

            double discountAmount = product.getPrice() * discountPercentage / 100.0;
            product.setDiscountedPrice(product.getPrice() - discountAmount);

        } else { // 2 aydan az süresi kaldı, bağışa çevir
            product.setDiscount(0);
            product.setPrice(0.0);
            product.setDiscountedPrice(0.0);
            product.setProductStatus(ProductStatus.DONATE);
        }
    }

    @Override
    public ProductDTO increaseQuantity(Long id, int amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found id: " + id));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        product.setQuantity(product.getQuantity()+amount);
        return saveAndMap(product);
    }

    @Override
    public ProductDTO decreaseQuantity(Long id, int amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found id: " + id));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (product.getQuantity() - amount < 0) {
            throw new IllegalStateException("Product quantity must be greater than 0.");
        }

        product.setQuantity(product.getQuantity()-amount);

        return saveAndMap(product);
    }



    private ProductDTO saveAndMap(Product product) {
        Product savedProduct = productRepository.save(product);

        return productMapper.map(savedProduct);
    }
}
