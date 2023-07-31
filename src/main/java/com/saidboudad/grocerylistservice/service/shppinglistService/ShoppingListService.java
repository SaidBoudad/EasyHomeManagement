package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.entity.ShoppingList;

import java.util.List;

public interface ShoppingListService {

    ShoppingList createShoppingList(ShoppingList shoppingList);
    ShoppingList getShoppingListById(Long listId);
    ShoppingList updateShoppingList(Long listId, ShoppingList shoppingList);
    boolean deleteShoppingListById(Long listId);
    List<ShoppingList> getShoppingListsByUserId(Long userId);



}
