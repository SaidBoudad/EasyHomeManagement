package com.saidboudad.grocerylistservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    //Get home page
    @GetMapping("/home")
    @PreAuthorize("hasRole('USER')")
    public String getHomePage(Model model) {
        return "home-page";
    }
    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public String home(Model model) {
        return "redirect:/home";
    }

}
