package com.saidboudad.grocerylistservice.service.item;

import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.User;
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
    public Item getItemByIdAndUser(Long itemId, User user) {
        return itemRepository.findByIdAndUser(itemId, user);
    }

    @Override
    public List<Item> getItemsByUser(User user) {
        return itemRepository.findByUser(user);
    }

    @Override
    public boolean deleteItemByIdAndUser(Long itemId, User user) {
        Item itemToDelete = itemRepository.findByIdAndUser(itemId, user);
        if (itemToDelete != null) {
            itemRepository.delete(itemToDelete);
            return true;
        }
        return false;
    }

}
