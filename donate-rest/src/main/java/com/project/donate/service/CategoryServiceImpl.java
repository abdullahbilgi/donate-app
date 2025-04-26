package com.project.donate.service;

import com.project.donate.dto.CategoryDTO;
import com.project.donate.exception.ResourceNotFoundException;
import com.project.donate.mapper.CategoryMapper;
import com.project.donate.model.Category;
import com.project.donate.repository.CategoryRepository;
import com.project.donate.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
        return categoryRepository.findById(id)
                .map(categoryMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found id: " + id));
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.mapDto(categoryDTO);

        return saveAndMap(category);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        getCategoryById(id);
        Category savingCategory = categoryMapper.mapDto(categoryDTO);
        savingCategory.setId(id);

        return saveAndMap(savingCategory);
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

            categoryRepository.deleteById(id);
        }
    }

    private CategoryDTO saveAndMap(Category category) {
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.map(savedCategory);
    }
}