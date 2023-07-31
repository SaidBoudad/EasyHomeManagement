package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.service.item.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Item>> getAllItemsForUser(@PathVariable Long userId) {
        List<Item> items = itemService.getAllItemsForUser(userId);
        return ResponseEntity.ok(items);
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


    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item newItem = itemService.addItem(item);
        return ResponseEntity.ok(newItem);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> putItem(@PathVariable Long itemId, @RequestBody Item updatedItem) {
        Item item = itemService.updateItem(itemId, updatedItem);
        if (item != null) {
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        boolean deleted = itemService.deleteItemById(itemId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();

    }


}
