package com.kpi.diploma.controller;

import com.kpi.diploma.model.entity.Client;
import com.kpi.diploma.model.entity.Order;
import com.kpi.diploma.model.entity.types.DeliveryType;
import com.kpi.diploma.model.entity.types.OrderType;
import com.kpi.diploma.repo.ClientRepo;
import com.kpi.diploma.repo.ProductRepo;
import com.kpi.diploma.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ClientRepo clientRepo;
    private final ProductRepo productRepo;

    /**
     * Перелік замовлень усіх клієнтів, які мають компанії
     */
    @GetMapping
    public String listOrders(Model model) {
        List<Client> clientsWithCompanies = clientRepo.findAll().stream()
                .filter(client -> client.getCompany() != null)
                .toList();

        List<Order> orders = clientsWithCompanies.stream()
                .flatMap(client -> orderService.getAllOrdersByCompany(client.getCompany().getId()).stream())
                .toList();

        model.addAttribute("orders", orders);
        return "orders/order-list";
    }

    /**
     * Форма створення нового замовлення
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("clients", clientRepo.findAll());
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("deliveryTypes", DeliveryType.values());
        model.addAttribute("orderTypes", OrderType.values());
        return "orders/order-create";
    }

    /**
     * Створення нового замовлення
     */
    @PostMapping("/create")
    public String createOrder(@RequestParam Long clientId,
                              @RequestParam OrderType orderType,
                              @RequestParam DeliveryType deliveryType,
                              @RequestParam(required = false) String additionalInfo,
                              @RequestParam List<Long> productIds,
                              @RequestParam List<Integer> quantities) {

        orderService.createOrder(clientId, orderType, deliveryType, additionalInfo, productIds, quantities);
        return "redirect:/orders";
    }

    /**
     * Перегляд замовлення
     */
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Замовлення не знайдено"));

        model.addAttribute("order", order);
        return "orders/order-view";
    }

    /**
     * Видалення замовлення
     */
    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Замовлення не знайдено"));

        model.addAttribute("order", order);
        model.addAttribute("clients", clientRepo.findAll());
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("deliveryTypes", DeliveryType.values());
        model.addAttribute("orderTypes", OrderType.values());

        return "orders/order-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Long id,
                              @RequestParam Long clientId,
                              @RequestParam OrderType orderType,
                              @RequestParam DeliveryType deliveryType,
                              @RequestParam(required = false) String additionalInfo,
                              @RequestParam List<Long> productIds,
                              @RequestParam List<Integer> quantities) {

        orderService.updateOrder(id, clientId, orderType, deliveryType, additionalInfo, productIds, quantities);
        return "redirect:/orders";
    }
}
