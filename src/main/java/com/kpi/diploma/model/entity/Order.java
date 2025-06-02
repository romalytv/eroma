package com.kpi.diploma.model.entity;

import com.kpi.diploma.model.entity.types.DeliveryType;
import com.kpi.diploma.model.entity.types.OrderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Клієнт, який зробив замовлення
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Тип (статус) замовлення: NEW, CONFIRMED, PAID, CANCELLED
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    // Тип доставки: Processing Shipped Delivered Rejected
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false)
    private DeliveryType deliveryType;

    // Додаткова інформація до замовлення
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    // Товари, пов’язані через OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    public Order() {}

    public Order(Client client, OrderType orderType, DeliveryType deliveryType, String additionalInfo) {
        this.client = client;
        this.orderType = orderType;
        this.deliveryType = deliveryType;
        this.additionalInfo = additionalInfo;
    }
}

