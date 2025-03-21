package com.project.donate.service;

import com.project.donate.dto.CategoryDTO;


import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id);

    void deleteCategory(Long id);
}
