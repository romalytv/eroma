package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.*;
import com.kpi.diploma.model.entity.types.DeliveryType;
import com.kpi.diploma.model.entity.types.OrderType;
import com.kpi.diploma.repo.ClientRepo;
import com.kpi.diploma.repo.OrderItemRepo;
import com.kpi.diploma.repo.OrderRepo;
import com.kpi.diploma.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ClientRepo clientRepo;
    private final ProductRepo productRepo;

    /**
     * Повертає всі замовлення певної компанії
     */
    public List<Order> getAllOrdersByCompany(Long companyId) {
        return orderRepo.findByCompanyId(companyId);
    }

    /**
     * Повертає одне замовлення за ID
     */
    public Optional<Order> getOrderById(Long id) {
        return orderRepo.findById(id);
    }

    /**
     * Створює нове замовлення (з прив’язкою клієнта, компанії, товарів)
     */
    public Order createOrder(Long clientId,
                             OrderType orderType,
                             DeliveryType deliveryType,
                             String additionalInfo,
                             List<Long> productIds,
                             List<Integer> quantities) {

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Клієнт не знайдений"));

        Company company = client.getCompany();
        if (company == null) {
            throw new IllegalStateException("Клієнт не прив’язаний до компанії");
        }

        Order order = new Order();
        order.setClient(client);
        order.setOrderType(orderType);
        order.setDeliveryType(deliveryType);
        order.setAdditionalInfo(additionalInfo);
        order.setCompany(company);

        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            Product product = productRepo.findById(productIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Товар не знайдено"));

            Integer quantity = quantities.get(i);
            BigDecimal unitPrice = product.getPrice(); // фіксуємо ціну на момент покупки

            OrderItem item = new OrderItem(order, product, quantity, unitPrice);
            items.add(item);
        }

        order.setItems(items);

        return orderRepo.save(order); // OrderItem буде збережено завдяки cascade = ALL
    }

    /**
     * Видалення замовлення за ID
     */
    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    public void updateOrder(Long orderId,
                            Long clientId,
                            OrderType orderType,
                            DeliveryType deliveryType,
                            String additionalInfo,
                            List<Long> productIds,
                            List<Integer> quantities) {

        Order existingOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Замовлення не знайдено"));

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Клієнта не знайдено"));

        Company company = client.getCompany();
        if (company == null) {
            throw new IllegalStateException("Клієнт не має компанії");
        }

        existingOrder.setClient(client);
        existingOrder.setOrderType(orderType);
        existingOrder.setDeliveryType(deliveryType);
        existingOrder.setAdditionalInfo(additionalInfo);
        existingOrder.setCompany(company);

        // Очистити старі товари й додати нові
        List<OrderItem> updatedItems = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            Product product = productRepo.findById(productIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Товар не знайдено"));
            Integer quantity = quantities.get(i);
            BigDecimal unitPrice = product.getPrice();

            updatedItems.add(new OrderItem(existingOrder, product, quantity, unitPrice));
        }

        existingOrder.getItems().clear();
        existingOrder.getItems().addAll(updatedItems);

        orderRepo.save(existingOrder); // CascadeType.ALL оновить OrderItem
    }

}
