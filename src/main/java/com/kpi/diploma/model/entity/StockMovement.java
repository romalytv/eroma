package com.kpi.diploma.model.entity;

import com.kpi.diploma.model.entity.types.MovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Продукт, який рухається
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Початковий склад (може бути null, якщо тип IN)
    @ManyToOne
    @JoinColumn(name = "from_warehouse_id")
    private Warehouse fromWarehouse;

    // Кінцевий склад (може бути null, якщо тип OUT)
    @ManyToOne
    @JoinColumn(name = "to_warehouse_id")
    private Warehouse toWarehouse;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    public StockMovement() {}

    public StockMovement(Product product, Warehouse fromWarehouse, Warehouse toWarehouse,
                         Integer quantity, MovementType movementType, LocalDateTime date, String additionalInfo) {
        this.product = product;
        this.fromWarehouse = fromWarehouse;
        this.toWarehouse = toWarehouse;
        this.quantity = quantity;
        this.movementType = movementType;
        this.date = date;
        this.additionalInfo = additionalInfo;
    }
}
