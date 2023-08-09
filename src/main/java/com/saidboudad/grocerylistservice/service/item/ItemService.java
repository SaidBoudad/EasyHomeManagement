package com.saidboudad.grocerylistservice.service.item;

import com.saidboudad.grocerylistservice.DTOs.ItemRequestDTO;
import com.saidboudad.grocerylistservice.entity.Item;

import java.util.List;

public interface ItemService {

    Item addItem(ItemRequestDTO itemRequestDTO);
    Item getItemById(Long itemId);
    List<Item> getAllItemsForUser(Long userId);
    List<Item> getAllItemsForList(Long listId);
    Item updateItem(Long itemId, ItemRequestDTO itemRequestDTO);
    boolean deleteItemById(Long itemId);


}
