package com.example.online_store.controller;

import com.example.online_store.entity.Product;
import com.example.online_store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductRepository productRepository;

    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute(
                "products",
                productRepository.findAll()
        );

        return "admin/dashboard";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {

        model.addAttribute(
                "product",
                new Product()
        );

        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(
            @ModelAttribute Product product
    ) {

        productRepository.save(product);

        return "redirect:/admin";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model
    ) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product not found"
                                )
                        );

        model.addAttribute(
                "product",
                product
        );

        return "admin/product-form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id
    ) {

        productRepository.deleteById(id);

        return "redirect:/admin";
    }
}
