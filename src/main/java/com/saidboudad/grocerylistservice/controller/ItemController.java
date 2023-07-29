package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.service.item.ItemService;
import com.saidboudad.grocerylistservice.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {


    private final ItemService itemService;
    private final UserService userService;

    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    // Endpoint for adding a new item to the shopping list
    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item, Principal principal) {
        // Get the authenticated user's username from Principal
        String username = principal.getName();

        // Find the user based on the username
        User user = userService.getUserByUsername(username);

        // Associate the item with the authenticated user
        item.setUser(user);

        Item newItem = itemService.addItem(item);
        return ResponseEntity.ok(newItem);
    }

    // Endpoint for retrieving a specific item by its ID
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable Long itemId, Principal principal) {
        // Get the authenticated user's username from Principal
        String username = principal.getName();

        // Find the user based on the username
        User user = userService.getUserByUsername(username);

        // Retrieve the item only if it belongs to the authenticated user
        Item item = itemService.getItemByIdAndUser(itemId, user);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint for retrieving all items of the authenticated user
    @GetMapping
    public ResponseEntity<List<Item>> getAllItemsForUser(Principal principal) {
        // Get the authenticated user's username from Principal
        String username = principal.getName();

        // Find the user based on the username
        User user = userService.getUserByUsername(username);

        List<Item> items = itemService.getItemsByUser(user);
        return ResponseEntity.ok(items);
    }

    // ... Implement other endpoints as needed ...

    // Endpoint for deleting an item from the shopping list
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId, Principal principal) {
        // Get the authenticated user's username from Principal
        String username = principal.getName();

        // Find the user based on the username
        User user = userService.getUserByUsername(username);

        // Delete the item only if it belongs to the authenticated user
        boolean deleted = itemService.deleteItemByIdAndUser(itemId, user);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


