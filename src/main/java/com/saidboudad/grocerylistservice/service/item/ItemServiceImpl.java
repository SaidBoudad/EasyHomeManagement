package com.saidboudad.grocerylistservice.service.item;

import com.saidboudad.grocerylistservice.DTOs.ItemRequestDTO;
import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.repository.ItemRepository;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import com.saidboudad.grocerylistservice.repository.UserRepository;
import com.saidboudad.grocerylistservice.service.shppinglistService.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, ShoppingListRepository shoppingListRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item addItem(ItemRequestDTO itemRequestDTO) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(itemRequestDTO.getShoppingListId());
        Optional<User> userOptional = userRepository.findById(itemRequestDTO.getUserId());
        if (shoppingListOptional.isPresent() && userOptional.isPresent()) {
            User user = userOptional.get();
            ShoppingList shoppingList = shoppingListOptional.get();
            Item item = new Item();
            item.setName(itemRequestDTO.getName());
            item.setCategory(itemRequestDTO.getCategory());
            item.setQuantity(itemRequestDTO.getQuantity());
            item.setShoppingList(shoppingList);
            item.setUser(user);
            itemRepository.save(item);
            System.out.println(item.getId().toString()+"  after ");
            return item;
        } else {
            throw new UserNotFoundException(itemRequestDTO.getUserId());
        }
    }


    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    public List<Item> getAllItemsForUser(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    public List<Item> getAllItemsForList(Long listId) {
        return itemRepository.findAllByShoppingListId(listId);
    }

    @Override
    public Item updateItem(Long itemId, ItemRequestDTO itemRequestDTO) {
        Item existingItem = itemRepository.findById(itemId).orElse(null);
        if (existingItem != null) {
            // Update the properties of the existingItem with the properties of updatedItem
            existingItem.setName(itemRequestDTO.getName());
            existingItem.setCategory(itemRequestDTO.getCategory());
            existingItem.setQuantity(itemRequestDTO.getQuantity());
            return itemRepository.save(existingItem);
        }
        return null; // Item with the given ID not found
    }

    @Override
    public boolean deleteItemById(Long itemId) {
        if (itemRepository.existsById(itemId)) {
            itemRepository.deleteById(itemId);
            System.out.println(itemRepository.findById(itemId).get().getName());
            return true;
        }
        return false;
    }
}
