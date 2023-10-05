package com.saidboudad.grocerylistservice.security.Service;

import com.saidboudad.grocerylistservice.security.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Convert the username to lowercase
        String lowercaseUsername = username.toLowerCase();
        AppUser appUser=accountService.loadUserByUsername(lowercaseUsername);
        if (appUser == null) {
            throw new UsernameNotFoundException("Client with username : %s not found " + username);
        }
        // Convert the custom User entity to Spring Security's UserDetails
        String[] roles=appUser.getRoles().stream().map(u->u.getRole()).toArray(String[]::new);
        UserDetails userDetails = User
                .withUsername(username)
                .password(appUser.getPassword())
                .roles(roles)
                .build();

        return userDetails;
    }
}
