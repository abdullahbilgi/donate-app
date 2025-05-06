package com.project.donate.repository;

import com.project.donate.model.CartProduct;
import com.project.donate.model.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {
    boolean existsByIdCartIdAndIdProductId(Long cartId, Long productId);

    CartProduct findByIdCartIdAndIdProductId(Long cartId, Long productId);
}
