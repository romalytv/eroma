package com.kpi.diploma.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String unit; // Наприклад: кг, шт, л

    @Column(nullable = false)
    private Integer quantity;

    // Шлях до зображення (необов'язковий)
    private String imagePath;

    @ManyToMany(mappedBy = "products")
    private List<Client> clients;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    public Product() {}

    public Product(String name, String code, String description, BigDecimal price, String unit, Integer quantity, String imagePath) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }
}


