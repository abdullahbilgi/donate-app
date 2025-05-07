package com.project.donate.repository;

import com.project.donate.enums.Status;
import com.project.donate.model.CartProduct;
import com.project.donate.model.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {
    boolean existsByIdCartIdAndIdProductIdAndStatus(Long cartId, Long productId, Status status);

    CartProduct findByIdCartIdAndIdProductIdAndStatus(Long cartId, Long productId,Status status);
}
