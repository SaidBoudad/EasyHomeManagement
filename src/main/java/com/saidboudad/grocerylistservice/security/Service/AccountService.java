package com.saidboudad.grocerylistservice.security.Service;

import com.saidboudad.grocerylistservice.security.entities.AppRole;
import com.saidboudad.grocerylistservice.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String username,String password,String email,String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username,String role);
    void removeRoleToUser(String username,String role);
    AppUser loadUserByUsername(String username);

}
