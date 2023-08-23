package com.saidboudad.grocerylistservice.security.repo;

import com.saidboudad.grocerylistservice.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser,String> {
    AppUser findByUsername(String username);
}
