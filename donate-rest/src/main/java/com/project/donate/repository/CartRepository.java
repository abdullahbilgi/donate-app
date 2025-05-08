package com.project.donate.repository;

import com.project.donate.enums.Status;
import com.project.donate.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    //List<Cart> findByUserIdOrderByPurchaseDateDesc(Long userId);

    Cart findByUserIdAndStatus(Long userId, Status status);

    List<Cart> findAllByUserIdAndStatus(Long userId, Status status);

}
