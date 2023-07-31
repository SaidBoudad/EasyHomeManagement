package com.saidboudad.grocerylistservice.service.item;

import com.saidboudad.grocerylistservice.entity.Item;

import java.util.List;

public interface ItemService {

    Item addItem(Item item);
    Item getItemById(Long itemId);
    public List<Item> getAllItemsForUser(Long userId);
    Item updateItem(Long itemId, Item updatedItem);
    boolean deleteItemById(Long itemId);

}
