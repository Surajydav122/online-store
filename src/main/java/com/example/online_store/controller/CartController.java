package com.example.online_store.controller;

import com.example.online_store.entity.CartItem;
import com.example.online_store.entity.Product;
import com.example.online_store.entity.User;
import com.example.online_store.service.CartService;
import com.example.online_store.service.ProductService;
import com.example.online_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;


    /*
     * ============================
     * VIEW CART
     * ============================
     */
    @GetMapping
    public String viewCart(Authentication authentication, Model model) {

        User user = getLoggedInUser(authentication);

        List<CartItem> cartItems = cartService.getCartItems(user);

        BigDecimal cartTotal = cartService.getCartTotal(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);

        return "cart";
    }


    /*
     * ============================
     * ADD PRODUCT TO CART
     * ============================
     */
    @PostMapping("/add/{productId}")
    public String addToCart(
            @PathVariable Long productId,
            Authentication authentication) {

        User user = getLoggedInUser(authentication);

        Product product = productService .getProductById(productId);

        cartService.addToCart(user, product);

        return "redirect:/cart";
    }


    /*
     * ============================
     * INCREASE QUANTITY
     * ============================
     */
    @PostMapping("/increase/{id}")
    public String increaseQuantity(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getLoggedInUser(authentication);

        cartService.increaseQuantity(id, user);

        return "redirect:/cart";
    }


    /*
     * ============================
     * DECREASE QUANTITY
     * ============================
     */
    @PostMapping("/decrease/{id}")
    public String decreaseQuantity(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getLoggedInUser(authentication);

        cartService.decreaseQuantity(id, user);

        return "redirect:/cart";
    }


    /*
     * ============================
     * REMOVE ITEM FROM CART
     * ============================
     */
    @PostMapping("/remove/{id}")
    public String removeFromCart(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getLoggedInUser(authentication);

        cartService.removeFromCart(id, user);

        return "redirect:/cart";
    }


    /*
     * ============================
     * GET LOGGED-IN USER
     * ============================
     */
    private User getLoggedInUser(Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException("Please login first");
        }

        String email = authentication.getName();

        return userService.getUserByEmail(email);
    }
}