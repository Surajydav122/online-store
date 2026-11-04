package com.example.online_store.service;
import com.example.online_store.entity.Product;
import com.example.online_store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getSaleProducts() {
        return productRepository.findByOnSaleTrue();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Product not found")
                );
    }
}
