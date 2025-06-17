package com.project.donate.repository;

import com.project.donate.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFind() {
        Category category = new Category();
        category.setName("CatTest1");

        Category cat1=categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(cat1.getId());

        assertTrue(foundCategory.isPresent());


    }
}
