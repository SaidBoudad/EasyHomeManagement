package com.saidboudad.grocerylistservice.service.item;

import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    public List<Item> getAllItemsForUser(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public boolean deleteItemById(Long itemId) {
        if (itemRepository.existsById(itemId)) {
            itemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }

    @Override
    public Item updateItem(Long itemId, Item updatedItem) {
        Item existingItem = itemRepository.findById(itemId).orElse(null);
        if (existingItem != null) {
            // Update the properties of the existingItem with the properties of updatedItem
            existingItem.setName(updatedItem.getName());
            existingItem.setCategory(updatedItem.getCategory());
            existingItem.setQuantity(updatedItem.getQuantity());
            // ... Add other properties if needed
            return itemRepository.save(existingItem);
        }
        return null; // Item with the given ID not found
    }
}
