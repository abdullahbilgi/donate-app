package com.project.donate.repository;

import com.project.donate.model.Category;
import com.project.donate.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    //List<Product> findAllByIsActiveTrue();

    Page<Product> findAllByIsActiveTrue(Pageable pageable);

    Page<Product> findAllByCategory_IdAndIsActiveTrue(Long categoryId, Pageable pageable);

    Page<Product> findAllByMarket_Address_Region_IdAndIsActiveTrue(Long regionId, Pageable pageable);
    // Şehre (City) göre aktif ürünleri getirir
    Page<Product> findAllByMarket_Address_Region_City_IdAndIsActiveTrue(Long cityId, Pageable pageable);

    Page<Product> findAllByMarketIdAndIsActiveTrue(Long cityId, Pageable pageable);
}
