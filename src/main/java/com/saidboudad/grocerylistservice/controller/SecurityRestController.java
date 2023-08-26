package com.saidboudad.grocerylistservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SecurityRestController {
    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication){
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        return authentication;

    }

}
