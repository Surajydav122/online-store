package com.example.online_store.repository;

import com.example.online_store.entity.CartItem;
import com.example.online_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProductId(
            User user,
            Long productId
    );
}
