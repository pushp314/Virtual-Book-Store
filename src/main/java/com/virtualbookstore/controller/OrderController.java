package com.virtualbookstore.controller;

import com.virtualbookstore.model.Order;
import com.virtualbookstore.model.User;
import com.virtualbookstore.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getUserOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders"; // Thymeleaf template name
    }

    @GetMapping("/details/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model) {
        Optional<Order> order = orderService.getOrderById(orderId);
        order.ifPresent(o -> model.addAttribute("order", o));
        return "order-details"; // Create a separate page for order details if needed
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order) {
        User user = order.getUser();
        BigDecimal totalPrice = order.getTotalPrice();
        orderService.createOrder(user, totalPrice);
        return "redirect:/orders"; // Redirect back to the orders page
    }

    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/orders"; // Redirect back to the orders page after deletion
    }
}
