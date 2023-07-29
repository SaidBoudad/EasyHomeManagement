package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by username
    User findByUsername(String username);
}