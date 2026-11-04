package com.example.online_store.controller;
import com.example.online_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor



public class HomeController {
    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        model.addAttribute(
                "saleProducts",
                productService.getSaleProducts()
        );

        return "home";
    }
}