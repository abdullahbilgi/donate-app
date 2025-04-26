package com.project.donate.service;


import com.project.donate.dto.ProductDTO;
import com.project.donate.enums.ProductStatus;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.ProductMapper;
import com.project.donate.model.Product;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
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
        log.info("{} - looked product with id: {}", GeneralUtil.extractUsername(), id);
        return productRepository.findById(id)
                .map(productMapper::map)
                .orElseThrow(() -> {
                    log.error("{} product not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Product not found id: " + id);
                });
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.mapDto(productDTO);
        calculateDiscountPrice(product);
        return saveAndMap(product,"save");
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {

        getProductById(id);
        Product savingProduct = productMapper.mapDto(productDTO);
        savingProduct.setId(id);
        calculateDiscountPrice(savingProduct);

        return saveAndMap(savingProduct,"update");
    }

    @Override
    public void deleteProduct(Long id) {
        ProductDTO productDTO = getProductById(id);
        Product product = productMapper.mapDto(productDTO);
        product.setIsActive(false);
        saveAndMap(product,"delete");

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

        log.info("All product discounts updated");
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
    public void increaseQuantity(Long id, int amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} Product not found id: {}", GeneralUtil.extractUsername(), id);
                    return new RuntimeException("Product not found id: " + id);
                });

        if (amount <= 0) {
            log.warn("Amount must be greater than 0.");
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        product.setQuantity(product.getQuantity() + amount);
        log.info("{} increase Quantity product with id: {}", GeneralUtil.extractUsername(), id);
        productRepository.save(product);
    }

    @Override
    public void decreaseQuantity(Long id, int amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} Product not found id: {}", GeneralUtil.extractUsername(), id);
                    return new RuntimeException("Product not found id: " + id);
                });

        if (amount <= 0) {
            log.warn("Amount must be greater than 0.");
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (product.getQuantity() - amount < 0) {
            log.warn("Product quantity must be greater than 0.");
            throw new IllegalStateException("Product quantity must be greater than 0.");
        }

        product.setQuantity(product.getQuantity() - amount);

        log.info("{} decrease Quantity product with id: {}", GeneralUtil.extractUsername(), id);
        productRepository.save(product);
    }


    private ProductDTO saveAndMap(Product product,String status) {
        Product savedProduct = productRepository.save(product);

        switch (status) {
            case "save" -> log.info("{} Created product: {}", GeneralUtil.extractUsername(), product);
            case "update" -> log.info("{} Updated product: {}", GeneralUtil.extractUsername(), product);
            case "delete" -> log.info("{} Deleted product: {}", GeneralUtil.extractUsername(), product);
        }

        return productMapper.map(savedProduct);
    }
}
