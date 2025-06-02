package com.kpi.diploma.service;

import com.kpi.diploma.model.entity.Payment;
import com.kpi.diploma.repo.PaymentRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;

    public PaymentService(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public List<Payment> findAll() {
        return paymentRepo.findAll();
    }

    public List<Payment> findByOrderId(Long orderId) {
        return paymentRepo.findByOrderId(orderId);
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepo.findById(id);
    }

    public Payment save(Payment payment) {
        return paymentRepo.save(payment);
    }

    public void deleteById(Long id) {
        paymentRepo.deleteById(id);
    }
}
