package com.virtualbookstore.controller;

import com.virtualbookstore.model.Cart;
import com.virtualbookstore.model.User;
import com.virtualbookstore.service.CartService;
import com.virtualbookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Cart>> getUserCart(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(cartService.getCartItems(user.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long bookId = Long.parseLong(payload.get("bookId").toString());
            Integer quantity = Integer.parseInt(payload.getOrDefault("quantity", 1).toString()); // Default to 1

            Cart cartItem = cartService.addToCart(user.getId(), bookId, quantity);
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok("Item removed successfully");
    }


    @PutMapping("/update")
    public ResponseEntity<Cart> updateCartQuantity(@RequestBody Map<String, Object> payload) {
    Long cartId = Long.parseLong(payload.get("id").toString());
    Integer quantity = Integer.parseInt(payload.get("quantity").toString());
    
    Cart updatedCart = cartService.updateQuantity(cartId, quantity);
    return ResponseEntity.ok(updatedCart);
}

}
