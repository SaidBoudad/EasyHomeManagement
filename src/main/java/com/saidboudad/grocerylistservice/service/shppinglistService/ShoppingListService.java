package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.exceptions.UserNotFoundException;

import java.util.List;

public interface ShoppingListService {

    public ShoppingList createShoppingList(String name, Long userId) throws UserNotFoundException;
    ShoppingList getShoppingListById(Long listId);
    ShoppingList updateShoppingList(Long listId, ShoppingList shoppingList);
    boolean deleteShoppingListById(Long listId);

    List<ShoppingList> getShoppingListsByUserId(Long userId);



}
