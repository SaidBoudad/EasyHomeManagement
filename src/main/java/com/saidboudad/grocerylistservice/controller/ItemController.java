package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.DTOs.ItemRequestDTO;
import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.service.client.ClientService;
import com.saidboudad.grocerylistservice.service.item.ItemService;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private ShoppingListService shoppingListService;
    private ClientService clientService;

    public ItemController(ItemService itemService, ShoppingListService shoppingListService, ClientService clientService) {

        this.itemService = itemService;
        this.shoppingListService = shoppingListService;
        this.clientService = clientService;
    }

    //add item to a list of client
    @PostMapping("/user/add")
    public String addItem(@ModelAttribute("itemRequest") ItemRequestDTO itemRequestDto,
                          Model model) {
        Item createdItem = itemService.addItem(itemRequestDto);
        if (createdItem != null) {
            ShoppingList shoppingList = shoppingListService.getShoppingListById(itemRequestDto.getShoppingListId());
            model.addAttribute("listDetails", shoppingList);
            return "list-details";
        } else {
            return "bad request";
        }
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

    // Get all items for a given client ID
    @GetMapping("/user/{clientId}")
    public ResponseEntity<List<Item>> getAllItemsForClient(@PathVariable Long clientId) {
        List<Item> items = itemService.getAllItemsForUser(clientId);
        return ResponseEntity.ok(items);
    }

    // Get all items for a given list ID
    @GetMapping("/user/list/{listId}")
    public List<Item> getAllItemsForList(@PathVariable Long listId) {
        return itemService.getAllItemsForList(listId);
    }


    @PutMapping("/user/{itemId}")
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
