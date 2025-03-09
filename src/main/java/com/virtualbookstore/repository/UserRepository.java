package com.virtualbookstore.repository;

import com.virtualbookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);  // ✅ Check if user exists by email
    boolean existsByEmail(String email);  // ✅ Add this method to fix the error
}
