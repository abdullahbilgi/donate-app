package com.project.donate.service;

import com.project.donate.dto.CategoryDTO;
import com.project.donate.dto.Request.CategoryRequest;
import com.project.donate.dto.Response.CategoryResponse;
import com.project.donate.model.Category;


import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    void deleteCategory(Long id);

    Category getCategoryEntityById(Long id);
}
