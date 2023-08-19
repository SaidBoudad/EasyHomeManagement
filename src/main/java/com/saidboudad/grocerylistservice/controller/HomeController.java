package com.saidboudad.grocerylistservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {

    //Get home page
    @GetMapping("/home")
    public String homehome(Model model) {
        return "home-page";
    }
    @GetMapping("/")
    public String home(Model model) {
        return "redirect:/home";
    }

}
