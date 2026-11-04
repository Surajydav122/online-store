package com.example.online_store.repository;
import com.example.online_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByOnSaleTrue();

    List<Product> findByStockGreaterThan(int stock);
}
