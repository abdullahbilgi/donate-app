package com.project.donate.mapper;

import com.project.donate.dto.CategoryDTO;
import com.project.donate.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements ObjectMapper<Category, CategoryDTO> {

    @Override
    public CategoryDTO map(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public Category mapDto(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .build();
    }
}