package com.virtualbookstore.service;

import com.virtualbookstore.model.Order;
import com.virtualbookstore.model.Payment;
import com.virtualbookstore.repository.OrderRepository;
import com.virtualbookstore.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Process a payment for the given order. The transactionId is provided by the user.
     * If a payment already exists for the order, update it.
     */
    @Transactional
    public Payment processPayment(Long orderId, String paymentMethod, String transactionId) {
        // Check if a payment already exists for this order
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();
            payment.setPaymentMethod(paymentMethod);
            payment.setTransactionId(transactionId);
            payment.setStatus("PENDING");
            return paymentRepository.save(payment);
        }
        // Retrieve the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        // Create a new Payment with the provided transactionId.
        Payment payment = new Payment(order, paymentMethod, transactionId, "PENDING");
        return paymentRepository.save(payment);
    }

    /**
     * Accept a payment (update its status to COMPLETED) – for admin use.
     */
    @Transactional
    public Payment acceptPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        payment.setStatus("COMPLETED");
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }
    
    public Iterable<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
