package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.DTOs.UserDTO;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.exceptions.UserNotFoundException;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import com.saidboudad.grocerylistservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {
    private final UserRepository userRepository;
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListServiceImpl(UserRepository userRepository, ShoppingListRepository shoppingListRepository) {
        this.userRepository = userRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public ShoppingList createShoppingList(String name, Long userId) throws UserNotFoundException {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setName(name);
            shoppingList.setUser(user);
            shoppingListRepository.save(shoppingList);
            System.out.println(shoppingList.getId().toString()+"  after ");
            shoppingList.setUserDTO(user.toDTO());
            return shoppingList;
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public ShoppingList getShoppingListById(Long listId) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(listId);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Optional<User> userOptional = Optional.ofNullable(shoppingList.getUser());
            userOptional.ifPresent(user -> {
                UserDTO userDTO = user.toDTO();
                shoppingList.setUserDTO(userDTO);
            });
            return shoppingList;
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public ShoppingList updateShoppingList(Long listId, ShoppingList shoppingList) {
        ShoppingList existingList = shoppingListRepository.findById(listId).orElse(null);
        if (existingList != null) {
            List<String> changes = new ArrayList<>();

            if (!existingList.getName().equals(shoppingList.getName())) {
                changes.add("Name changed from '" + existingList.getName() + "' to '" + shoppingList.getName() + "'");
                existingList.setName(shoppingList.getName());
            }
            if (existingList.getNumberOfItem() != shoppingList.getNumberOfItem()) {
                changes.add("Number of items changed from '" + existingList.getNumberOfItem() + "' to '" + shoppingList.getNumberOfItem() + "'");
                existingList.setNumberOfItem(shoppingList.getNumberOfItem());
            }
            if (!existingList.getItems().equals(shoppingList.getItems())) {
                changes.add("Items changed from " + existingList.getItems() + " to " + shoppingList.getItems());
                existingList.setItems(shoppingList.getItems());
            }

            // Add more checks for other fields I want to track and update them accordingly.

            if (!changes.isEmpty()) {
                // Fetch the associated User entity and convert it to UserDTO
                User user = existingList.getUser();
                UserDTO userDTO = user.toDTO();

                // Create a message containing the list of changes and the modification time
                String modificationMessage = String.join(", ", changes);
                existingList.setUserDTO(userDTO);
                existingList.setLastChange(modificationMessage);

                // Save the changes to the database
                return shoppingListRepository.save(existingList);
            }
        }
        return null;
    }

    @Override
    public boolean deleteShoppingListById(Long listId) {
        if (shoppingListRepository.existsById(listId)) {
            shoppingListRepository.deleteById(listId);
            return true;
        }
        return false;
    }

    // Method to get all shopping lists for a specific user by ID
    @Override
    public List<ShoppingList> getShoppingListsByUserId(Long userId) {
        List<ShoppingList> shoppingLists = shoppingListRepository.findByUserId(userId);
        // Loop through the list and convert User to UserDTO
        shoppingLists.forEach(shoppingList -> {
            User user = shoppingList.getUser();
            if (user != null) {
                UserDTO userDTO = user.toDTO();
                shoppingList.setUserDTO(userDTO);
            }
        });
        //using stream
        // Use stream to convert User to UserDTO for each ShoppingList
//        shoppingLists.stream()
//                .map(ShoppingList::getUser) // Get the User for each ShoppingList
//                .filter(Objects::nonNull) // Filter out null Users
//                .map(User::toDTO) // Convert User to UserDTO
//                .forEach(userDTO -> {
//                    shoppingLists.forEach(shoppingList -> {
//                        if (shoppingList.getUser() != null && shoppingList.getUser().getId().equals(userDTO.getId())) {
//                            shoppingList.setUserDTO(userDTO); // Set UserDTO for the matching ShoppingList
//                        }
//                    });
//                });

        return shoppingLists;
    }
}

