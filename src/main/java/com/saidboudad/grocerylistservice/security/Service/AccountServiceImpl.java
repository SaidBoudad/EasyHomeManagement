package com.saidboudad.grocerylistservice.security.Service;

import com.saidboudad.grocerylistservice.security.entities.AppRole;
import com.saidboudad.grocerylistservice.security.entities.AppUser;
import com.saidboudad.grocerylistservice.security.repo.AppRoleRepo;
import com.saidboudad.grocerylistservice.security.repo.AppUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AppUserRepo appUserRepo;
    private AppRoleRepo appRoleRepo;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {
        AppUser appUser = appUserRepo.findByUsername(username);
        if(appUser!=null) throw new RuntimeException("username already exist");
        if(!password.equals(confirmPassword)) throw new RuntimeException("password not much");
        appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        AppUser savedAppUser = appUserRepo.save(appUser);
        return savedAppUser;
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole = appRoleRepo.findById(role).orElse(null);
        if(appRole!=null) throw new RuntimeException("role already existe");
        appRole = AppRole.builder()
                .role(role)
                .build();
        return appRoleRepo.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String role) {
        AppUser appUser = appUserRepo.findByUsername(username);
        AppRole appRole = appRoleRepo.findById(role).get();
        appUser.getRoles().add(appRole);
        appUserRepo.save(appUser);
    }

    @Override
    public void removeRoleToUser(String username, String role) {
        AppUser appUser = appUserRepo.findByUsername(username);
        AppRole appRole = appRoleRepo.findById(role).get();
        appUser.getRoles().remove(appRole);
        appUserRepo.save(appUser);

    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepo.findByUsername(username);
    }
}
