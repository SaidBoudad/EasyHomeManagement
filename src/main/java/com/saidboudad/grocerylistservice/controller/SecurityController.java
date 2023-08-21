package com.saidboudad.grocerylistservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    @GetMapping("/notAuthorized")
    public String getNotAuthorizedPage(){
        return "notAuthorized";
    }
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
}
