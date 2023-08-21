package com.saidboudad.grocerylistservice.service.item;

import com.saidboudad.grocerylistservice.DTOs.ItemRequestDTO;
import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.repository.ItemRepository;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import com.saidboudad.grocerylistservice.repository.ClientRepository;
import com.saidboudad.grocerylistservice.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final ClientRepository clientRepository;

    public ItemServiceImpl(ItemRepository itemRepository, ShoppingListRepository shoppingListRepository, ClientRepository clientRepository) {
        this.itemRepository = itemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Item addItem(ItemRequestDTO itemRequestDTO) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(itemRequestDTO.getShoppingListId());
        Optional<Client> userOptional = clientRepository.findById(itemRequestDTO.getClientId());
        if (shoppingListOptional.isPresent() && userOptional.isPresent()) {
            Client client = userOptional.get();
            ShoppingList shoppingList = shoppingListOptional.get();
            Item item = new Item();
            item.setName(itemRequestDTO.getName());
            item.setCategory(itemRequestDTO.getCategory());
            item.setQuantity(itemRequestDTO.getQuantity());
            item.setShoppingList(shoppingList);
            item.setClient(client);
            itemRepository.save(item);
            System.out.println(item.getId().toString()+"  after ");
            return item;
        } else {
            throw new UserNotFoundException(itemRequestDTO.getClientId());
        }
    }


    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    public List<Item> getAllItemsForUser(Long clientId) {
        return itemRepository.findByClientId(clientId);
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
    @Transactional
    public boolean deleteItemById(Long itemId) {
        if (itemRepository.existsById(itemId)) {
            itemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }
}
