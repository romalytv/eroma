package com.kpi.diploma.controller;

import com.kpi.diploma.model.entity.Transaction;
import com.kpi.diploma.model.entity.types.TransactionType;
import com.kpi.diploma.service.CompanyService;
import com.kpi.diploma.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CompanyService companyService;

    public TransactionController(TransactionService transactionService, CompanyService companyService) {
        this.transactionService = transactionService;
        this.companyService = companyService;
    }

    // Список транзакцій з фільтром по компанії
    @GetMapping
    public String viewTransactions(@RequestParam(required = false) Long companyId, Model model) {
        List<Transaction> transactions = (companyId != null)
                ? transactionService.findByCompanyId(companyId)
                : transactionService.findAll();

        model.addAttribute("transactions", transactions);
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("selectedCompanyId", companyId);

        return "transactions/transaction-view";
    }

    // Створення транзакції — сторінка форми
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("types", TransactionType.values());
        return "transactions/transaction-create";
    }

    // Обробка збереження
    @PostMapping("/create")
    public String createTransaction(@ModelAttribute("transaction") Transaction transaction) {
        transaction.setDate(LocalDateTime.now());
        transactionService.save(transaction);
        return "redirect:/transactions";
    }

    // Видалення транзакції
    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
        return "redirect:/transactions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        model.addAttribute("transaction", transaction);
        model.addAttribute("companies", companyService.getAllCompanies());
        model.addAttribute("types", TransactionType.values());

        return "transactions/transaction-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTransaction(@PathVariable Long id,
                                    @ModelAttribute("transaction") Transaction updatedTransaction) {
        transactionService.updateTransaction(id, updatedTransaction);
        return "redirect:/transactions";
    }
}
