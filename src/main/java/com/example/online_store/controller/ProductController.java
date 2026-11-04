package com.example.online_store.controller;

import com.example.online_store.service.ProductService;
import com.example.online_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")

public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public String productDetails(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute(
                "product",
                productService.getProduct(id)
        );

        return "product-details";
    }
}
