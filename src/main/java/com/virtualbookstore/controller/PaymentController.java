package com.virtualbookstore.controller;

import com.virtualbookstore.model.Payment;
import com.virtualbookstore.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Process a payment for the given orderId. Expects a query parameter for paymentMethod
     * and a JSON body with the transactionId.
     */
    @PostMapping("/process/{orderId}")
    public ResponseEntity<Payment> processPayment(
            @PathVariable Long orderId,
            @RequestParam String paymentMethod,
            @RequestBody Map<String, Object> payload) {
        String transactionId = (String) payload.get("transactionId");
        if (transactionId == null || transactionId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Payment payment = paymentService.processPayment(orderId, paymentMethod, transactionId);
        return ResponseEntity.ok(payment);
    }

    /**
     * Admin endpoint to accept a payment (set status to COMPLETED).
     */
    @PutMapping("/accept/{paymentId}")
    public ResponseEntity<Payment> acceptPayment(@PathVariable Long paymentId) {
        Payment updatedPayment = paymentService.acceptPayment(paymentId);
        return ResponseEntity.ok(updatedPayment);
    }

    /**
     * Retrieve payment details by order ID.
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrder(@PathVariable Long orderId) {
        Optional<Payment> payment = paymentService.getPaymentByOrderId(orderId);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve all payments (for admin dashboard).
     */
    @GetMapping
    public ResponseEntity<Iterable<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
