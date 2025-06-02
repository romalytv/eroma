package com.kpi.diploma.model.entity;

import com.kpi.diploma.model.entity.types.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Payment() {}

    public Payment(LocalDateTime paymentDate, BigDecimal amount, PaymentType paymentType, Order order) {
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentType = paymentType;
        this.order = order;
    }
}
