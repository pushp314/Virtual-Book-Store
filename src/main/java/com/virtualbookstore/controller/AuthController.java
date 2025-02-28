package com.virtualbookstore.controller;

import com.virtualbookstore.model.User;
import com.virtualbookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Login API
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String email, @RequestParam String password) {
        Optional<User> user = userService.authenticate(email, password);
        if (user.isPresent()) {
            return Map.of("message", "Login successful", "role", user.get().getRole());
        } else {
            return Map.of("message", "Invalid email or password");
        }
    }

    // Logout API (No actual logic, handled by Spring Security)
    @PostMapping("/logout")
    public Map<String, String> logout() {
        return Map.of("message", "Logout successful");
    }
}
