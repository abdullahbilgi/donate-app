package com.project.donate.service;

import com.project.donate.dto.CategoryDTO;
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
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        log.info("{} looked category with id: {}", GeneralUtil.extractUsername(), id);
        return categoryRepository.findById(id)
                .map(categoryMapper::map)
                .orElseThrow(() -> {
                    log.error("{} Category not found id: {}", GeneralUtil.extractUsername(), id);
                    return new RuntimeException("Category not found id: " + id);
                });
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.mapDto(categoryDTO);

        return saveAndMap(category,"save");
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        getCategoryById(id);
        Category savingCategory = categoryMapper.mapDto(categoryDTO);
        savingCategory.setId(id);

        return saveAndMap(savingCategory,"update");
    }

    @Override
    public void deleteCategory(Long id) {
        CategoryDTO categoryDto = getCategoryById(id);
        if (!Objects.isNull(categoryDto)) {
            Category category = categoryMapper.mapDto(categoryDto);

            productRepository.findByCategory(category).forEach(product -> {
                product.setCategory(null);
                productRepository.save(product);
            });
            log.info("{} Category deleted: {}", GeneralUtil.extractUsername(), id);
            categoryRepository.deleteById(id);
        }
    }

    private CategoryDTO saveAndMap(Category category, String status) {
        Category savedCategory = categoryRepository.save(category);

        if (status.equals("save")) {
            log.info("{} Created category: {}", GeneralUtil.extractUsername(), category);
        } else if (status.equals("update")) {
            log.info("{} Updated category: {}", GeneralUtil.extractUsername(), category);
        } else {
            log.info("{} Deleted category: {}", GeneralUtil.extractUsername(), category);
        }

        return categoryMapper.map(savedCategory);
    }
}