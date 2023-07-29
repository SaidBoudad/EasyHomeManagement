package com.saidboudad.grocerylistservice.service.user;

import com.saidboudad.grocerylistservice.entity.User;

public interface UserService {

    User createUser(User user);
    User getUserById(Long userId);
    User getUserByUsername(String username);
    User updateUser(Long userId, User user);
    boolean deleteUserById(Long userId);

}