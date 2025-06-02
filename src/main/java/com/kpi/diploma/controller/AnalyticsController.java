package com.kpi.diploma.controller;

import com.kpi.diploma.service.ClientService;
import com.kpi.diploma.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private final TransactionService transactionService;
    private final ClientService clientService;

    public AnalyticsController(TransactionService transactionService, ClientService clientService) {
        this.transactionService = transactionService;
        this.clientService = clientService;
    }

    @GetMapping
    public String showAnalyticsPage(@RequestParam(name = "companyId", required = false) Long companyId, Model model) {
        if (companyId == null) {
            companyId = 1L; // тимчасовий ID або з твоєї бази
        }

        model.addAttribute("weeklyData", transactionService.getProfitVsExpense("WEEK", companyId));
        model.addAttribute("monthlyData", transactionService.getProfitVsExpense("MONTH", companyId));
        model.addAttribute("yearlyData", transactionService.getProfitVsExpense("YEAR", companyId));

        model.addAttribute("feedbackStats", clientService.getFeedbackStats());

        return "analytics/analytics-view";
    }
}
