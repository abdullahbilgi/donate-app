package com.project.donate.service;

import com.project.donate.dto.Request.CategoryRequest;
import com.project.donate.dto.Response.CategoryResponse;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CategoryMapper;
import com.project.donate.model.Category;
import com.project.donate.repository.CategoryRepository;
import com.project.donate.repository.ProductRepository;
import com.project.donate.util.GeneralUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        log.info("{} looked category with id: {}", GeneralUtil.extractUsername(), id);
        return categoryMapper.mapToDto(getCategoryEntityById(id));
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.mapToEntity(request);
        return saveAndMap(category,"save");
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        getCategoryById(id);
        Category savingCategory = categoryMapper.mapToEntity(request);
        savingCategory.setId(id);
        return saveAndMap(savingCategory,"update");
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryEntityById(id);
        if (!Objects.isNull(category)) {
            productRepository.findByCategory(category).forEach(product -> {
                product.setCategory(null);
                productRepository.save(product);
            });
            log.info("{} Category deleted: {}", GeneralUtil.extractUsername(), id);
            category.setIsActive(false);
        }
        log.info("{} Deleted category: {}", GeneralUtil.extractUsername(), category);
        categoryRepository.save(category);
    }

    @Override
    public Category getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("{} Category not found id: {}", GeneralUtil.extractUsername(), id);
                    return new ResourceNotFoundException("Category not found id: " + id);
                });
    }

    private CategoryResponse saveAndMap(Category category, String status) {
        Category savedCategory = categoryRepository.save(category);

        if (status.equals("save")) {
            log.info("{} Created category: {}", GeneralUtil.extractUsername(), category);
        } else {
            log.info("{} Updated category: {}", GeneralUtil.extractUsername(), category);
        }
        return categoryMapper.mapToDto(savedCategory);
    }
}