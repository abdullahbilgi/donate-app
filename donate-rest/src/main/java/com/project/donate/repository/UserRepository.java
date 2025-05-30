package com.project.donate.repository;


import com.project.donate.model.Cart;
import com.project.donate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String userName);
    Optional<User> findUserByCart(Cart cart);
    User findByEmail(String email);
}
