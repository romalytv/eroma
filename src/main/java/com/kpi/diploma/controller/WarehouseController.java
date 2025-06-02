package com.kpi.diploma.controller;

import com.kpi.diploma.model.entity.InventoryItem;
import com.kpi.diploma.model.entity.Product;
import com.kpi.diploma.model.entity.Warehouse;
import com.kpi.diploma.repo.ProductRepo;
import com.kpi.diploma.service.InventoryItemService;
import com.kpi.diploma.service.ProductService;
import com.kpi.diploma.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    private final ProductService productService;

    private final InventoryItemService inventoryItemService;


    public WarehouseController(WarehouseService warehouseService, ProductService productService, InventoryItemService inventoryItemService) {
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.inventoryItemService = inventoryItemService;
    }

    // 📦 Перегляд залишків
    @GetMapping
    public String viewInventory(Model model) {
        List<InventoryItem> items = warehouseService.getAllInventory();
        model.addAttribute("items", items);
        return "warehouse/inventory";
    }

    @GetMapping("/{id}")
    public String viewWarehouse(@PathVariable Long id, Model model) {
        Warehouse warehouse = warehouseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Склад не знайдено"));

        List<InventoryItem> items = inventoryItemService.findByWarehouseId(id);

        model.addAttribute("warehouse", warehouse);
        model.addAttribute("items", items);
        return "warehouse/warehouse-view";
    }

    @PostMapping("/update")
    public String updateItemQuantity(
            @RequestParam Long id,
            @RequestParam Integer quantity
    ) {
        InventoryItem item = inventoryItemService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setQuantity(quantity);
        inventoryItemService.save(item);
        return "redirect:/warehouse";
    }


    // ➕ Додавання товару
    @PostMapping("/in")
    public String addProduct(@RequestParam Long productId,
                             @RequestParam Long warehouseId,
                             @RequestParam Integer quantity) {
        warehouseService.processInMovement(productId, warehouseId, quantity);
        return "redirect:/warehouse";
    }

    // ➖ Списання товару
    @PostMapping("/out")
    public String removeProduct(@RequestParam Long productId,
                                @RequestParam Long warehouseId,
                                @RequestParam Integer quantity) {
        warehouseService.processOutMovement(productId, warehouseId, quantity);
        return "redirect:/warehouse";
    }

    // 🔄 Переміщення товару між складами
    @PostMapping("/transfer")
    public String transferProduct(@RequestParam Long productId,
                                  @RequestParam Long fromWarehouseId,
                                  @RequestParam Long toWarehouseId,
                                  @RequestParam Integer quantity) {
        warehouseService.processTransfer(productId, fromWarehouseId, toWarehouseId, quantity);
        return "redirect:/warehouse";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        model.addAttribute("product", product);
        return "warehouse/product-view";
    }
}
