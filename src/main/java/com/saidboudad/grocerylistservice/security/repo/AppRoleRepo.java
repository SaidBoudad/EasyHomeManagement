package com.saidboudad.grocerylistservice.security.repo;

import com.saidboudad.grocerylistservice.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepo extends JpaRepository<AppRole,String> {

}
