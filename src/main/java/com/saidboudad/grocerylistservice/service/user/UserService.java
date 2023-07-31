package com.saidboudad.grocerylistservice.service.user;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User getUserById(Long userId);
    User updateUser(Long userId, User user);
    boolean deleteUserById(Long userId);
    List<ShoppingList> getShoppingListsByUserId(Long userId);
}
