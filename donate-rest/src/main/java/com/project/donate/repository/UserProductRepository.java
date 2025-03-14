package com.project.donate.repository;

import com.project.donate.model.Address;
import com.project.donate.model.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProductRepository extends JpaRepository<UserProduct, Long> {
}
