package com.saidboudad.grocerylistservice.service.user;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.exceptions.DuplicateEmailException;
import com.saidboudad.grocerylistservice.exceptions.DuplicateUsernameException;
import com.saidboudad.grocerylistservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        // Check for duplicate email
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new DuplicateEmailException(user.getEmail());
        }

        // Check for duplicate username
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new DuplicateUsernameException(user.getUsername());
        }

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
            // Check if the new email is already used by another user
            User userWithNewEmail = userRepository.findByEmail(user.getEmail());
            if (userWithNewEmail != null && !userWithNewEmail.getId().equals(userId)) {
                throw new DuplicateEmailException("Email is already in use by another user");
            }

            // Check if the new username is already used by another user
            User userWithNewUsername = userRepository.findByUsername(user.getUsername());
            if (userWithNewUsername != null && !userWithNewUsername.getId().equals(userId)) {
                throw new DuplicateUsernameException("Username is already in use by another user");
            }

            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            // Don't forget to handle password encoding.

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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Page<User> getUsersByPage(String keyword ,int page, int size,String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), "id"); // Create Sort object
        Pageable pageable = PageRequest.of(page, size, sort); // Apply sort to pageable
        return userRepository.findByUsernameContains(keyword,pageable);

    }


}
