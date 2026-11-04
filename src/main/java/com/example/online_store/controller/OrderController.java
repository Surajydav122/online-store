package com.example.online_store.controller;

import ch.qos.logback.core.model.Model;
import com.example.online_store.entity.User;
import com.example.online_store.repository.OrderRepository;
import com.example.online_store.service.OrderService;
import com.example.online_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserService userService;

    @PostMapping("/place")
    public String placeOrder(
            Authentication authentication
    ) {

        User user =
                userService.findByEmail(
                        authentication.getName()
                );

        orderService.placeOrder(user);

        return "redirect:/orders";
    }

    @GetMapping
    public String orders(
            Authentication authentication,
            Model model
    ) {

        User user =
                userService.findByEmail(
                        authentication.getName()
                );

        model.addAttribute(
                "orders",
                orderRepository
                        .findByUserOrderByOrderDateDesc(user)
        );

        return "orders";
    }
}