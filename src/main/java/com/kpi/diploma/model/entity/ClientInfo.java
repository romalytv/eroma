package com.kpi.diploma.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "client_info")
public class ClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Односторонній зв’язок до клієнта
    @OneToOne
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private Client client;

    @Column(columnDefinition = "TEXT")
    private String detailsText;

    // Шлях до зображення (необов'язковий)
    private String imagePath;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public ClientInfo() {}

    public ClientInfo(Client client, String detailsText, String imagePath) {
        this.client = client;
        this.detailsText = detailsText;
        this.imagePath = imagePath;
    }
}
