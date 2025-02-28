package com.virtualbookstore.controller;

import com.virtualbookstore.model.Cart;
import com.virtualbookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public List<Cart> getUserCart(@PathVariable Long userId) {
        return cartService.getCartItems(userId);
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    @DeleteMapping("/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }
}

// New Controller for Thymeleaf Cart Page
@Controller
@RequestMapping("/cart")
class CartViewController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public String getCartPage(@PathVariable Long userId, Model model) {
        List<Cart> cartItems = cartService.getCartItems(userId);
        model.addAttribute("cartItems", cartItems);
        return "cart"; // Renders cart.html inside templates/
    }
}
