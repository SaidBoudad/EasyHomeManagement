package com.saidboudad.grocerylistservice.service.user;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User getUserById(Long userId);
    User updateUser(Long userId, User user);
    boolean deleteUserById(Long userId);
    List<ShoppingList> getShoppingListsByUserId(Long userId);
    List<User> getAllUsers();

    Page<User> getUsersByPage(String keyword,int page, int size);
}
