package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.DTOs.ShoppingListRequest;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import com.saidboudad.grocerylistservice.service.shppinglistService.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingList")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final ShoppingListRepository shoppingListRepository;


    public ShoppingListController(ShoppingListService shoppingListService, ShoppingListRepository shoppingListRepository) {
        this.shoppingListService = shoppingListService;
        this.shoppingListRepository = shoppingListRepository;
    }

    // Endpoint to create specific shoppingList the request body has the listName and the userId
    @PostMapping
    public ResponseEntity<ShoppingList> createShoppingList(@RequestBody ShoppingListRequest request) throws UserNotFoundException {
        try{
            ShoppingList shoppingList = shoppingListService.createShoppingList(request.getName(), request.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(shoppingList);
        }catch (UserNotFoundException e) {
            return handleUserNotFoundException(e);
        }
    }
    // Exception handler method to handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ShoppingList> handleUserNotFoundException(UserNotFoundException ex) {
        // To add an appropriate error response here
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
            // Here I use the lastChange string to display the last change to the user
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
