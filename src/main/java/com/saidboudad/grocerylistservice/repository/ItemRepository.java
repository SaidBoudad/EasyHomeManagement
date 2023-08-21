package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


    // Custom query method to find a specific item by its ID and client
    Item findByIdAndClient(Long itemId, Client client);
    List<Item> findByClientId(Long clientId);


    // Custom query method to find a specific item by its ID and list ID
    Item findByIdAndShoppingListId(Long itemId, Long listId);
    // Add a custom query method to find all items for a given list ID
    List<Item> findAllByShoppingListId(Long listId);


    // Custom query method to delete a specific item by its ID and user
    void deleteByIdAndClient(Long itemId, Client client);
}
