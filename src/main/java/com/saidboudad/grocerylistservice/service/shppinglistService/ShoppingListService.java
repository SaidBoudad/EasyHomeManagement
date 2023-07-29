package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.entity.ShoppingList;

public interface ShoppingListService {

    ShoppingList createShoppingList(ShoppingList shoppingList);
    ShoppingList getShoppingListById(Long listId);
    ShoppingList updateShoppingList(Long listId, ShoppingList shoppingList);
    boolean deleteShoppingListById(Long listId);


}
