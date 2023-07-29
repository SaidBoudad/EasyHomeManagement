package com.saidboudad.grocerylistservice.service.item;

import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.User;

import java.util.List;

public interface ItemService {

    Item addItem(Item item);
    Item getItemByIdAndUser(Long itemId, User user);
    List<Item> getItemsByUser(User user);
    boolean deleteItemByIdAndUser(Long itemId, User user);

}
