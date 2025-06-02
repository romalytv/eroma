package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepo extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByFromWarehouse_IdOrToWarehouse_Id(Long fromId, Long toId);
}
