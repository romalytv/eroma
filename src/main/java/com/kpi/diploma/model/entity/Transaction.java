package com.kpi.diploma.model.entity;

import com.kpi.diploma.model.entity.types.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    // Опціонально пов’язане замовлення (може бути null)
    @ManyToOne
    @JoinColumn(name = "related_order_id")
    private Order relatedOrder;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public Transaction() {}

    public Transaction(LocalDateTime date, String description, TransactionType transactionType,
                       BigDecimal amount, Order relatedOrder, Company company) {
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.amount = amount;
        this.relatedOrder = relatedOrder;
        this.company = company;
    }
}
