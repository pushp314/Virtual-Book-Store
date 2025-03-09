package com.virtualbookstore.controller;

import com.virtualbookstore.model.Order;
import com.virtualbookstore.model.User;
import com.virtualbookstore.repository.UserRepository;
import com.virtualbookstore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order details by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create an order
    // The frontend sends only the totalPrice in the JSON body.
    // The authenticated user is derived from the Principal.
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(Principal principal, @RequestBody Map<String, Object> request) {
        try {
            String email = principal.getName();
            System.out.println("Creating order for user: " + email);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
            // Ensure the request contains "totalPrice"
            if (!request.containsKey("totalPrice")) {
                throw new RuntimeException("Missing 'totalPrice' in request body");
            }
            BigDecimal totalPrice = new BigDecimal(request.get("totalPrice").toString());
            Order createdOrder = orderService.createOrder(user, totalPrice);
            return ResponseEntity.ok(createdOrder);
        } catch(Exception e) {
            e.printStackTrace(); // Print the full stack trace for debugging
            return ResponseEntity.status(500).body(null);
        }
    }

    // Delete an order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully!");
    }
}
