package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.InventoryItem;
import com.kpi.diploma.model.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> { }

