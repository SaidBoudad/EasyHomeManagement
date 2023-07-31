package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public ShoppingList createShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }

    @Override
    public ShoppingList getShoppingListById(Long listId) {
        return shoppingListRepository.findById(listId).orElse(null);
    }

    @Override
    public ShoppingList updateShoppingList(Long listId, ShoppingList shoppingList) {
        ShoppingList existingList = shoppingListRepository.findById(listId).orElse(null);
        if (existingList != null) {
            existingList.setName(shoppingList.getName());
            existingList.setDateModified(LocalDateTime.now());
            return shoppingListRepository.save(existingList);
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
        return shoppingListRepository.findByUserId(userId);
    }

}

