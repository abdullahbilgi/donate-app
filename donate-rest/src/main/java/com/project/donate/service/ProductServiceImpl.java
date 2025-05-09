package com.project.donate.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.enums.ProductStatus;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.ProductMapper;
import com.project.donate.model.Category;
import com.project.donate.model.Market;
import com.project.donate.model.Product;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Cloudinary cloudinary;
    private final CategoryService categoryService;
    private final MarketService marketService;


    @Override
    public List<ProductResponse> getAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productMapper.mapToDto(getProductEntityById(id));
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryService.getCategoryEntityById(request.getCategoryId());
        Market market = marketService.getMarketEntityById(request.getMarketId());
        Product product = productMapper.mapToEntity(request);
        product.setCategory(category);
        product.setMarket(market);
        validProduct(product);
        return saveAndMap(product, "save");
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        getProductById(id);
        Product savingProduct = productMapper.mapToEntity(request);
        if (!savingProduct.getIsActive()){
            log.error("{} Product is not active!", GeneralUtil.extractUsername());
            throw new ResourceNotFoundException("Product is not active!");
        }
        Market market = marketService.getMarketEntityById(request.getMarketId());
        Category category = categoryService.getCategoryEntityById(request.getCategoryId());
        savingProduct.setCategory(category);
        savingProduct.setMarket(market);
        savingProduct.setId(id);
        validProduct(savingProduct);
        return saveAndMap(savingProduct, "update");
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductEntityById(id);
        product.setIsActive(false);
        log.info("{} Deleted product: {}", GeneralUtil.extractUsername(), product);
        productRepository.save(product);
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url"); // https link!
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public void updateProductImage(Long productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found id: " + productId));
        product.setImageUrl(imageUrl);
        productRepository.save(product);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    private void validProduct(Product product) {

        if (product.getProductStatus() == ProductStatus.DONATE) {
            product.setDiscount(0);
            product.setPrice(0.0);
            product.setDiscountedPrice(0.0);

        } else {
            if (product.getPrice() <= product.getDiscountedPrice()) {
                log.warn("{} The discounted price must be lower than the price!", GeneralUtil.extractUsername());
                throw new ArithmeticException("The discounted price must be lower than the price!");
            }
            double discountRate = ((product.getPrice() - product.getDiscountedPrice()) / product.getPrice()) * 100;
            product.setDiscount((int) Math.round(discountRate));
        }


        if (product.getExpiryDate() != null && product.getProductionDate() != null &&
                !product.getExpiryDate().isAfter(product.getProductionDate())) {
            log.warn("{} Expiry date must be after production date!", GeneralUtil.extractUsername());
            throw new IllegalArgumentException("Expiry date must be after production date!");
        }

        if (product.getLastDonatedDate() != null && product.getExpiryDate() != null &&
                !product.getLastDonatedDate().isEqual(product.getExpiryDate().minusDays(3))) {
            log.warn("{} Last donated date must be 3 days before expiry date!", GeneralUtil.extractUsername());
            throw new IllegalArgumentException("Last donated date must be 3 days before expiry date!");
        }
    }

    @Scheduled(cron = "0 1 0 * * ?") // Her gün saat 00:01:00'da çalışır
    public void updateAllProductStatus() {
        List<Product> products = productRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Product product : products) {
            if (product.getProductStatus() != ProductStatus.DONATE &&
                    product.getLastDonatedDate() != null &&
                    now.isAfter(product.getLastDonatedDate())) {

                product.setProductStatus(ProductStatus.DONATE);
                product.setDiscount(0);
                product.setPrice(0.0);
                product.setDiscountedPrice(0.0);

                productRepository.save(product);
            }
        }

        log.info("Product statuses updated after 00:01:00");
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

    @Override
    public Product getProductEntityById(Long id) {
        log.info("{} - looked product with id: {}", GeneralUtil.extractUsername(), id);

        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} product not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Product not found id: " + id);
                });
    }

    @Override
    public Page<ProductResponse> getAllProductsPageable(Pageable pageable) {
        return productRepository.findAllByIsActiveTrue(pageable)
                .map(productMapper::mapToDto);
    }

    private ProductResponse saveAndMap(Product product, String status) {
        Product savedProduct = productRepository.save(product);
        if (status.equals("save")) {
            log.info("{} Created product: {}", GeneralUtil.extractUsername(), product);
        } else {
            log.info("{} Updated product: {}", GeneralUtil.extractUsername(), product);
        }
        return productMapper.mapToDto(savedProduct);
    }
}
