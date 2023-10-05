package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.service.client.ClientService;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;


@Controller
public class HomeController {

    private final ShoppingListService shoppingListService;
    private final ClientService clientService;
    public HomeController(ShoppingListService shoppingListService, ClientService clientService) {
        this.shoppingListService = shoppingListService;
        this.clientService = clientService;
    }

    //Get home page with number of list in each category
    @GetMapping("/home")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getHomePageAuthenticated(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Client client = clientService.findByUsername(userDetails.getUsername().toLowerCase());
        // Get the categories and their counts for the user
        Map<String, Long> categoryCounts = shoppingListService.getCategoryCountsForUser(client.getClientName());
        model.addAttribute("client", client);
        // Bind the category counts to the category list
        model.addAttribute("categories", categoryCounts.keySet());
        model.addAttribute("categoryCounts", categoryCounts);
        return "home-page";
    }

    //Get home page for non-authenticated
    @GetMapping("/")
    public String homeForAll(Model model) {
        return "home-unauthenticated";
    }

//    @GetMapping("/signin")
//    public String getLoginPage(Model model) {
//        return "login";
//    }

    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        // Perform any additional logout logic if needed
        SecurityContextHolder.clearContext(); // Clear the security context
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the HttpSession
        }
        return "redirect:/login?logout=true"; // Redirect to the login page with a logout parameter
    }

}
