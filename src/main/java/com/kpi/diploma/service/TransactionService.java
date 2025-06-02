package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.Transaction;
import com.kpi.diploma.model.entity.types.TransactionType;
import com.kpi.diploma.repo.TransactionRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    public List<Transaction> findAll() {
        return transactionRepo.findAll();
    }

    public List<Transaction> findByCompanyId(Long companyId) {
        return transactionRepo.findByCompanyId(companyId);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepo.findById(id);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    public void deleteById(Long id) {
        transactionRepo.deleteById(id);
    }

    public void updateTransaction(Long id, Transaction updatedTransaction) {
        Transaction existing = transactionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Транзакція не знайдена"));

        existing.setTransactionType(updatedTransaction.getTransactionType());
        existing.setAmount(updatedTransaction.getAmount());
        existing.setDescription(updatedTransaction.getDescription());
        existing.setCompany(updatedTransaction.getCompany());
        existing.setRelatedOrder(updatedTransaction.getRelatedOrder());
        existing.setDate(LocalDateTime.now());

        transactionRepo.save(existing);
    }

    public Map<String, List<Object>> getProfitVsExpense(String period, Long companyId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;

        switch (period.toUpperCase()) {
            case "WEEK":
                start = now.minusDays(7);
                break;
            case "MONTH":
                start = now.minusMonths(1);
                break;
            case "YEAR":
                start = now.minusYears(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        List<Transaction> transactions = transactionRepo.findByCompanyIdAndDateBetween(companyId, start, now);

        Map<String, List<Object>> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<BigDecimal> profits = new ArrayList<>();
        List<BigDecimal> expenses = new ArrayList<>();

        // групуємо по даті
        Map<LocalDate, List<Transaction>> grouped = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getDate().toLocalDate()));
        grouped = new TreeMap<>(grouped); // сортування по даті

        for (Map.Entry<LocalDate, List<Transaction>> entry : grouped.entrySet()) {
            labels.add(entry.getKey().toString());

            BigDecimal profit = entry.getValue().stream()
                    .filter(t -> t.getTransactionType() == TransactionType.INCOME)
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal expense = entry.getValue().stream()
                    .filter(t -> t.getTransactionType() == TransactionType.EXPENSE)
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            profits.add(profit);
            expenses.add(expense);
        }

        result.put("labels", new ArrayList<>(labels));
        result.put("profit", new ArrayList<>(profits));
        result.put("expense", new ArrayList<>(expenses));

        return result;
    }

}
