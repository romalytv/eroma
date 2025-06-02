package com.kpi.diploma.repo;

import com.kpi.diploma.model.entity.Transaction;
import com.kpi.diploma.model.entity.types.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCompanyId(Long companyId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.transactionType = :type AND t.date BETWEEN :from AND :to")
    double sumAmountByTypeBetween(@Param("type") TransactionType type,
                                  @Param("from") LocalDateTime from,
                                  @Param("to") LocalDateTime to);

    List<Transaction> findByDateBetween(LocalDateTime from, LocalDateTime to);

    List<Transaction> findByCompanyIdAndDateBetween(Long companyId, LocalDateTime start, LocalDateTime end);
}
