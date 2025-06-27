package com.project.donate.repository;

import com.project.donate.enums.Status;
import com.project.donate.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //List<Cart> findByUserIdOrderByPurchaseDateDesc(Long userId);

    Optional<Cart> findByUserIdAndStatus(Long userId, Status status);

    List<Cart> findAllByUserIdAndStatus(Long userId, Status status);

    List<Cart> findAllByUserIdAndStatusOrderByPurchaseDateDesc(Long userId, Status status);

    @Query("""
    SELECT DISTINCT c
    FROM Cart c
    JOIN c.cartProducts cp
    JOIN cp.product p
    WHERE c.status = :status
      AND p.market.id = :marketId
    ORDER BY c.purchaseDate DESC
""")
    List<Cart> findApprovedCartsByMarketId(@Param("marketId") Long marketId, @Param("status") Status status);





}
