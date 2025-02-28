package com.virtualbookstore.controller;

import com.virtualbookstore.model.User;
import com.virtualbookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a New User (or Update Existing User)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user); // Password is hashed automatically
    }

    // Get All Users (Restricted to Admins)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers(); // Fixed type issue
    }

    // Get User by ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Update User Profile (User Can Update Name, Email, Password)
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // Delete User (Restricted to Admins)
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully!";
    }
}
