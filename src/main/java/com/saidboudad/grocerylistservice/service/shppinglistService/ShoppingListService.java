package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.DTOs.ListCategory;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Map;

public interface ShoppingListService {

    public ShoppingList createShoppingList(String name, Long userId, ListCategory category) throws UserNotFoundException;
    ShoppingList getShoppingListById(Long listId);
    ShoppingList updateShoppingList(Long id ,ShoppingList shoppingList);
    boolean deleteShoppingListById(Long listId);
    List<ShoppingList> getShoppingListsByClientId(Long userId);
    List<ShoppingList> getListsByCategoryAndUsername(ListCategory category,String username);
    Map<String, Long> getCategoryCountsForUser(String clientName);



}
