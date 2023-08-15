package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.DTOs.UserDTO;
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
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        UserDTO createdUserDTO = createdUser.toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    // Endpoint to get a specific user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            UserDTO userDTO = user.toDTO();
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update an existing user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        if (updatedUser != null) {
            UserDTO updatedUserDTO = updatedUser.toDTO();
            return ResponseEntity.ok(updatedUserDTO);
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
