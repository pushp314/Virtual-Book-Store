package com.virtualbookstore.service;

import com.virtualbookstore.model.Order;
import com.virtualbookstore.model.Payment;
import com.virtualbookstore.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Payment processPayment(Order order, String paymentMethod) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(UUID.randomUUID().toString()); // Mock Transaction ID
        payment.setStatus("COMPLETED"); // For now, assume all payments are successful
        return paymentRepository.save(payment);
    }
}
