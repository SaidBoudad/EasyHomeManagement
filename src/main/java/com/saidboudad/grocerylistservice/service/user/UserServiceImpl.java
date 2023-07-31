package com.saidboudad.grocerylistservice.service.user;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            // If you want to allow updating the password, you can do so here.
            // Don't forget to handle password encoding, similar to the previous implementation.

            return userRepository.save(existingUser);
        }
        return null;
    }

    @Override
    public boolean deleteUserById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    // Method to get all shopping lists for a specific user by ID
    @Override
    public List<ShoppingList> getShoppingListsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getShoppingLists();
        }
        return Collections.emptyList();
    }

}
