package com.project.donate.repository;

import com.project.donate.model.Category;
import com.project.donate.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findAllByIsActiveTrue();

    Page<Product> findAll(Pageable pageable);
}
