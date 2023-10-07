package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {
    // Custom query method to find a token by token string
    PasswordResetToken findByToken(String token);
}