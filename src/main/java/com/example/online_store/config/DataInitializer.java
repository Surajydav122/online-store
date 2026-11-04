package com.example.online_store.config;

import com.example.online_store.entity.Product;
import com.example.online_store.entity.User;
import com.example.online_store.repository.ProductRepository;
import com.example.online_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer
        implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createAdmin();

        createProducts();
    }

    private void createAdmin() {

        if (!userRepository.existsByEmail(
                "admin@gmail.com")) {

            User admin = User.builder()
                    .name("Administrator")
                    .email("admin@gmail.com")
                    .password(
                            passwordEncoder.encode(
                                    "admin123"
                            )
                    )
                    .role("ADMIN")
                    .build();

            userRepository.save(admin);
        }
    }

    private void createProducts() {

        if (productRepository.count() > 0) {
            return;
        }

        Product laptop = Product.builder()
                .name("Laptop")
                .description(
                        "High performance laptop " +
                                "for work and entertainment."
                )
                .price(60000)
                .salePrice(55000)
                .onSale(true)
                .stock(10)
                .imageUrl(
                        "https://via.placeholder.com/400"
                )
                .build();

        Product phone = Product.builder()
                .name("Smart Phone")
                .description(
                        "Modern smartphone with " +
                                "excellent performance."
                )
                .price(30000)
                .salePrice(28000)
                .onSale(true)
                .stock(20)
                .imageUrl(
                        "https://via.placeholder.com/400"
                )
                .build();

        Product headphones = Product.builder()
                .name("Headphones")
                .description(
                        "Wireless headphones " +
                                "with high quality sound."
                )
                .price(3000)
                .salePrice(0)
                .onSale(false)
                .stock(50)
                .imageUrl(
                        "https://via.placeholder.com/400"
                )
                .build();

        productRepository.save(laptop);
        productRepository.save(phone);
        productRepository.save(headphones);
    }
}