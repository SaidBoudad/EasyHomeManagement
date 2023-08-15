package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.DTOs.ItemRequestDTO;
import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.service.item.ItemService;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import com.saidboudad.grocerylistservice.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private ShoppingListService shoppingListService;
    private UserService userService;

    public ItemController(ItemService itemService, ShoppingListService shoppingListService, UserService userService) {

        this.itemService = itemService;
        this.shoppingListService = shoppingListService;
        this.userService = userService;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable Long itemId) {
        Item item = itemService.getItemById(itemId);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all items for a given user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Item>> getAllItemsForUser(@PathVariable Long userId) {
        List<Item> items = itemService.getAllItemsForUser(userId);
        return ResponseEntity.ok(items);
    }

    // Get all items for a given list ID
    @GetMapping("/list/{listId}")
    public List<Item> getAllItemsForList(@PathVariable Long listId) {
        return itemService.getAllItemsForList(listId);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody ItemRequestDTO itemRequest) {
        Item createdItem = itemService.addItem(itemRequest);
        if (createdItem != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> putItem(@PathVariable Long itemId, @RequestBody ItemRequestDTO itemRequest) {
        Item item = itemService.updateItem(itemId, itemRequest);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{itemId}")
    @Transactional
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        boolean deleted = itemService.deleteItemById(itemId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();

    }


}
