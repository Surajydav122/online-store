package com.example.online_store.service;

import com.example.online_store.entity.*;
import com.example.online_store.repository.CartItemRepository;
import com.example.online_store.repository.OrderItemRepository;
import com.example.online_store.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Order placeOrder(User user) {

        List<CartItem> cartItems =
                cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {

            throw new RuntimeException(
                    "Cart is empty"
            );
        }

        Order order =
                Order.builder()
                        .user(user)
                        .orderDate(
                                LocalDateTime.now()
                        )
                        .status("PLACED")
                        .totalAmount(0)
                        .build();

        double total = 0;

        for (CartItem cartItem : cartItems) {
            Product product =
                    cartItem.getProduct();

            if (cartItem.getQuantity()
                    > product.getStock()) {

                throw new RuntimeException(
                        "Not enough stock for "
                                + product.getName()
                );
            }

            double price =
                    product.isOnSale()
                            ? product.getSalePrice()
                            : product.getPrice();

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(
                                    cartItem.getQuantity()
                            )
                            .price(price)
                            .build();

            order.getItems()
                    .add(orderItem);

            total +=
                    price * cartItem.getQuantity();

            product.setStock(
                    product.getStock()
                            - cartItem.getQuantity()
            );
        }

        order.setTotalAmount(total);

        Order savedOrder =
                orderRepository.save(order);

        cartItemRepository.deleteAll(
                cartItems
        );
        return savedOrder;
    }
}