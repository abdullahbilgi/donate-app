package com.project.donate.service;

import com.cloudinary.Cloudinary;
import com.project.donate.dto.Request.ProductRequest;
import com.project.donate.dto.Request.UserDonateRequest;
import com.project.donate.dto.Response.ProductResponse;
import com.project.donate.dto.Response.UserDonateResponse;
import com.project.donate.mapper.ProductMapper;
import com.project.donate.mapper.UserDonateMapper;
import com.project.donate.model.Category;
import com.project.donate.model.Product;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDonateServiceImpl implements UserDonateService {

    private final ProductRepository productRepository;
    private final UserDonateMapper userDonateMapper;
    private final CategoryService categoryService;

    @Override
    public UserDonateResponse createProduct(UserDonateRequest request) {
        Category category =categoryService.getCategoryEntityById(request.getCategoryId());
        Product product = userDonateMapper.mapToEntity(request);
        product.setCategory(category);
        return saveAndMap(product);
    }

    private UserDonateResponse saveAndMap(Product product) {
        Product savedProduct = productRepository.save(product);

            log.info("{} saved donate product: {}", GeneralUtil.extractUsername(), product);

        return userDonateMapper.mapToDto(savedProduct);
    }
}
