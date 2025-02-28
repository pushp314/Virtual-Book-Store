package com.virtualbookstore.controller;

import com.virtualbookstore.model.Order;
import com.virtualbookstore.model.Payment;
import com.virtualbookstore.service.OrderService;
import com.virtualbookstore.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/process/{orderId}")
    public ResponseEntity<Payment> processPayment(@PathVariable Long orderId, @RequestParam String paymentMethod) {
        Optional<Order> orderOptional = orderService.getOrderById(orderId);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = orderOptional.get();
        Payment payment = paymentService.processPayment(order, paymentMethod);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrder(@PathVariable Long orderId) {
        Optional<Payment> payment = paymentService.getPaymentByOrderId(orderId);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
