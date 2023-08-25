package com.saidboudad.grocerylistservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    //Get home page
    @GetMapping("/home")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getHomePageAuthenticated(Model model) {
        return "home-page";
    }
    @GetMapping("/")
    public String homeForAll(Model model) {
        return "home-all";
    }
    @GetMapping("/signin")
    public String getLoginPage(Model model) {
        return "login";
    }
    @GetMapping("/logout")
    public String getHomeForAllFromLogin(Model model) {
        return "home-all";
    }
}
