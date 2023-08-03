package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-lists")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @PostMapping
    public ResponseEntity<ShoppingList> createShoppingList(@RequestBody ShoppingList shoppingList) {
        ShoppingList createdList = shoppingListService.createShoppingList(shoppingList);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
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

    @PutMapping("/{listId}")
    public ResponseEntity<ShoppingList> updateShoppingList(@PathVariable Long listId, @RequestBody ShoppingList shoppingList) {
        ShoppingList updatedList = shoppingListService.updateShoppingList(listId, shoppingList);
        if (updatedList != null) {
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
