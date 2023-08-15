package com.saidboudad.grocerylistservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/home")
    public String home(Model model) {
        return "home-page";
    }

    @GetMapping("/lists")
    public String lists(Model model) {
        return "shoppingLists";
    }

    @GetMapping("/user")
    public String user(Model model) {
        return "user";
    }

}
