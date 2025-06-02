package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.InventoryItem;
import com.kpi.diploma.model.entity.Product;
import com.kpi.diploma.model.entity.StockMovement;
import com.kpi.diploma.model.entity.Warehouse;
import com.kpi.diploma.model.entity.types.MovementType;
import com.kpi.diploma.repo.InventoryItemRepo;
import com.kpi.diploma.repo.ProductRepo;
import com.kpi.diploma.repo.StockMovementRepo;
import com.kpi.diploma.repo.WarehouseRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    private final InventoryItemRepo inventoryItemRepo;
    private final StockMovementRepo stockMovementRepo;
    private final ProductRepo productRepo;
    private final WarehouseRepo warehouseRepo;

    public WarehouseService(InventoryItemRepo inventoryItemRepo,
                            StockMovementRepo stockMovementRepo,
                            ProductRepo productRepo,
                            WarehouseRepo warehouseRepo) {
        this.inventoryItemRepo = inventoryItemRepo;
        this.stockMovementRepo = stockMovementRepo;
        this.productRepo = productRepo;
        this.warehouseRepo = warehouseRepo;
    }

    public List<InventoryItem> getAllInventory() {
        return inventoryItemRepo.findAll();
    }

    public Optional<InventoryItem> getInventoryItem(Long id) {
        return inventoryItemRepo.findById(id);
    }

    public void updateInventoryQuantities(List<Long> ids, List<Integer> quantities) {
        for (int i = 0; i < ids.size(); i++) {
            InventoryItem item = inventoryItemRepo.findById(ids.get(i)).orElseThrow();
            item.setQuantity(quantities.get(i));
            inventoryItemRepo.save(item);
        }
    }

    @Transactional
    public void processInMovement(Long productId, Long warehouseId, Integer quantity) {
        Product product = productRepo.findById(productId).orElseThrow();
        Warehouse warehouse = warehouseRepo.findById(warehouseId).orElseThrow();

        InventoryItem item = inventoryItemRepo
                .findByWarehouse(warehouse).stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new InventoryItem(product, warehouse, 0));

        item.setQuantity(item.getQuantity() + quantity);
        inventoryItemRepo.save(item);

        StockMovement movement = new StockMovement(
                product,
                null,
                warehouse,
                quantity,
                MovementType.IN,
                LocalDateTime.now(),
                "Додавання товару"
        );
        stockMovementRepo.save(movement);
    }

    @Transactional
    public void processOutMovement(Long productId, Long warehouseId, Integer quantity) {
        Product product = productRepo.findById(productId).orElseThrow();
        Warehouse warehouse = warehouseRepo.findById(warehouseId).orElseThrow();

        InventoryItem item = inventoryItemRepo
                .findByWarehouse(warehouse).stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Товар не знайдено на складі"));

        if (item.getQuantity() < quantity) {
            throw new IllegalStateException("Недостатньо товару для списання");
        }

        item.setQuantity(item.getQuantity() - quantity);
        inventoryItemRepo.save(item);

        StockMovement movement = new StockMovement(
                product,
                warehouse,
                null,
                quantity,
                MovementType.OUT,
                LocalDateTime.now(),
                "Списання товару"
        );
        stockMovementRepo.save(movement);
    }

    @Transactional
    public void processTransfer(Long productId, Long fromWarehouseId, Long toWarehouseId, Integer quantity) {
        // Зчитуємо товар на складі-відправнику
        InventoryItem fromItem = inventoryItemRepo.findByWarehouseIdAndProductId(fromWarehouseId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in from-warehouse"));

        if (fromItem.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock in from-warehouse");
        }

        // Віднімаємо кількість на складі-відправнику
        fromItem.setQuantity(fromItem.getQuantity() - quantity);
        inventoryItemRepo.save(fromItem);

        // Додаємо або оновлюємо товар на складі-приймачі
        InventoryItem toItem = inventoryItemRepo.findByWarehouseIdAndProductId(toWarehouseId, productId)
                .orElse(new InventoryItem(
                        fromItem.getProduct(),
                        warehouseRepo.findById(toWarehouseId).orElseThrow(),
                        0
                ));

        toItem.setQuantity(toItem.getQuantity() + quantity);
        inventoryItemRepo.save(toItem);

        // (Необов’язково) запис у StockMovement
        StockMovement movement = new StockMovement(
                fromItem.getProduct(),
                fromItem.getWarehouse(),
                toItem.getWarehouse(),
                quantity,
                MovementType.TRANSFER,
                LocalDateTime.now(),
                "Auto transfer"
        );
        stockMovementRepo.save(movement);
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouseRepo.findById(id);
    }
}
