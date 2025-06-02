package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.InventoryItem;
import com.kpi.diploma.model.entity.Product;
import com.kpi.diploma.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepo extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByWarehouseIdAndProductId(Long warehouseId, Long productId);
    List<InventoryItem> findByWarehouseId(Long warehouseId);

    List<InventoryItem> findByWarehouse(Warehouse warehouse);
}
