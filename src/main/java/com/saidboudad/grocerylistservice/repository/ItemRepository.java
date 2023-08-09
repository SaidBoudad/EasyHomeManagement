package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


    // Custom query method to find a specific item by its ID and user
    Item findByIdAndUser(Long itemId, User user);
    List<Item> findByUserId(Long userId);


    // Custom query method to find a specific item by its ID and list ID
    Item findByIdAndShoppingListId(Long itemId, Long listId);
    // Add a custom query method to find all items for a given list ID
    List<Item> findAllByShoppingListId(Long listId);


    // Custom query method to delete a specific item by its ID and user
    void deleteByIdAndUser(Long itemId, User user);
}
