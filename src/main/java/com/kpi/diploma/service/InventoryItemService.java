package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.InventoryItem;
import com.kpi.diploma.repo.InventoryItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryItemService {

    private final InventoryItemRepo inventoryItemRepository;

    public InventoryItemService(InventoryItemRepo inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItem> findAll() {
        return inventoryItemRepository.findAll();
    }

    public List<InventoryItem> findByWarehouseId(Long warehouseId) {
        return inventoryItemRepository.findByWarehouseId(warehouseId);
    }

    public InventoryItem save(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    public Optional<InventoryItem> findById(Long id) {
        return inventoryItemRepository.findById(id);
    }

    public void delete(Long id) {
        inventoryItemRepository.deleteById(id);
    }
}
