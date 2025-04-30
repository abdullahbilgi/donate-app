package com.project.donate.mapper;

import com.project.donate.dto.CategoryDTO;
import com.project.donate.dto.Request.CategoryRequest;
import com.project.donate.dto.Response.CategoryResponse;
import com.project.donate.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper  {

    public CategoryResponse mapToDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category mapToEntity(CategoryRequest request) {
        return Category.builder()
                .id(request.getId())
                .name(request.getName())
                .build();
    }
}