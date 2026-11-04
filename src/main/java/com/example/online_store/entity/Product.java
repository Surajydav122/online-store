package com.example.online_store.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    private double price;

    private double salePrice;

    private boolean onSale;

    private int stock;

    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
