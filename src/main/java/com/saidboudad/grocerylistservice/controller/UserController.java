package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import com.saidboudad.grocerylistservice.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ShoppingListService shoppingListService;

    public UserController(UserService userService, ShoppingListService shoppingListService) {
        this.userService = userService;
        this.shoppingListService = shoppingListService;
    }

    // Endpoint to create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Endpoint to get a specific user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update an existing user
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to get shopping lists for a specific user by ID
    @GetMapping("/{userId}/lists")
    public ResponseEntity<List<ShoppingList>> getShoppingListsForUser(@PathVariable Long userId) {
        List<ShoppingList> shoppingLists = shoppingListService.getShoppingListsByUserId(userId);
        return ResponseEntity.ok(shoppingLists);
    }


}
