package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.DTOs.ListCategory;
import com.saidboudad.grocerylistservice.DTOs.ShoppingListRequest;
import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.exceptions.UserNotFoundException;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import com.saidboudad.grocerylistservice.service.client.ClientService;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/list")
public class ShoppingListController {

    private  final ShoppingListService shoppingListService;
    private ClientService clientService;
    private final Logger log = LoggerFactory.getLogger(ShoppingListController.class);
    


    public ShoppingListController(ShoppingListService shoppingListService, ShoppingListRepository shoppingListRepository, ClientService clientService) {
        this.shoppingListService = shoppingListService;
        this.clientService = clientService;
    }

    // Display the list creation form
    @GetMapping("/user/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String showCreateListForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Client client = clientService.findByUsername(userDetails.getUsername());
        model.addAttribute("list", new ShoppingList());
        model.addAttribute("client", client);
        model.addAttribute("clientId", client.getId()); // Add clientId to the model
        return "create-list";
    }


    @PostMapping("/user/save")
    @PreAuthorize("hasRole('USER')")
    public String createShoppingList(@ModelAttribute("list") ShoppingList shoppingList,
                                     @RequestParam("clientId") Long clientId,
                                     Model model) {
        try {
            ShoppingList shoppingListCreated = shoppingListService.createShoppingList(shoppingList.getName(), clientId, shoppingList.getCategory());
            // Success message
            log.info(shoppingListCreated.getName());

            model.addAttribute("shoppingListCreated", shoppingListCreated);
            model.addAttribute("successMessage", "Shopping list created successfully!");
        } catch (UserNotFoundException e) {
            // Error message
            model.addAttribute("errorMessage", "User not found.");
        }
        return "create-list";
    }

    @GetMapping("/user/category/{category}")
    public String getListsByCategory(@PathVariable("category") ListCategory category, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Client client = clientService.findByUsername(userDetails.getUsername());
        List<ShoppingList> shoppingLists = shoppingListService.getListsByCategoryAndUsername(category, client.getClientName());
        Map<String, Long> categoryCounts = shoppingListService.getCategoryCountsForUser(client.getClientName());

        if (shoppingLists == null) {
            model.addAttribute("customMessage", "");
            model.addAttribute("categoryCounts", categoryCounts);
        } else {
            model.addAttribute("shoppingLists", shoppingLists);
            model.addAttribute("categoryCounts", categoryCounts);
        }
        return "home-page"; // Return the main template name
    }

    @GetMapping("/user/category-counts")
    @ResponseBody
    public Map<String, Long> getCategoryCountsForClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Client client = clientService.findByUsername(userDetails.getUsername());

        Map<String, Long> categoryCounts = shoppingListService.getCategoryCountsForUser(client.getClientName());

        return categoryCounts;
    }




    // Endpoint to get a specific shopping list by ID
//    @GetMapping("/{listId}")
//    @PreAuthorize("hasRole('USER')")
//    public String getShoppingListById(@PathVariable Long listId) {
//        ShoppingList shoppingList = shoppingListService.getShoppingListById(listId);
//        if (shoppingList != null) {
//            return null;
//        } else {
//            return null;
//        }
//    }
//








    //    This part is for the RestFull calls
    // Endpoint to create specific shoppingList the request body has the listName and the clientId
    @PostMapping
    public ResponseEntity<ShoppingList> createShoppingList(@RequestBody ShoppingListRequest request) throws UserNotFoundException {
        try {
            ShoppingList shoppingList = shoppingListService.createShoppingList(request.getName(), request.getClientId(),request.getCategory());
            return ResponseEntity.status(HttpStatus.CREATED).body(shoppingList);
        } catch (UserNotFoundException e) {
            return handleUserNotFoundException(e);
        }
    }

    // Exception handler method to handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ShoppingList> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Endpoint to get a specific shopping list by ID
    @GetMapping("/{listId}")
    public ResponseEntity<ShoppingList> getShoppingListById(@PathVariable Long listId) {
        ShoppingList shoppingList = shoppingListService.getShoppingListById(listId);
        if (shoppingList != null) {
            return ResponseEntity.ok(shoppingList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update a shoppingList by listId as a pathVariable
    @PutMapping("/{listId}")
    public ResponseEntity<ShoppingList> updateShoppingList(@PathVariable Long listId, @RequestBody ShoppingList shoppingList) {
        ShoppingList updatedList = shoppingListService.updateShoppingList(listId, shoppingList);
        if (updatedList != null) {
            String lastChange = updatedList.getLastChange();
            // Here I use the lastChange string to display the last change to the client
            System.out.println("Last Change: " + lastChange);
            return ResponseEntity.ok(updatedList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteShoppingList(@PathVariable Long listId) {
        boolean deleted = shoppingListService.deleteShoppingListById(listId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
