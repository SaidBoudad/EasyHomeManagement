package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.DTOs.ClientDTO;
import com.saidboudad.grocerylistservice.DTOs.ListCategory;
import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.Item;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.exceptions.UserNotFoundException;
import com.saidboudad.grocerylistservice.repository.ClientRepository;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ShoppingListServiceImpl implements ShoppingListService {
    private final ClientRepository clientRepository;
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListServiceImpl(ClientRepository clientRepository, ShoppingListRepository shoppingListRepository) {
        this.clientRepository = clientRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public ShoppingList createShoppingList(String name, Long clientId, ListCategory category) throws UserNotFoundException {

        Optional<Client> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setName(name);
            shoppingList.setClient(client);
            shoppingList.setCategory(category);
            shoppingListRepository.save(shoppingList);
            shoppingList.setClientDTO(client.toDTO());
            return shoppingList;
        } else {
            throw new UserNotFoundException(clientId);
        }
    }

    @Override
    public List<ShoppingList> getListsByCategoryAndUsername(ListCategory category,String clientName) {
        List<ShoppingList> shoppingLists = shoppingListRepository.findByCategoryAndClient_ClientName(category, clientName);
        if (shoppingLists.isEmpty()) {
            // No lists found for the specified category and client name
            return null;
        }
        return shoppingLists;
    }

    @Override
    public Map<String, Long> getCategoryCountsForUser(String clientName) {
        List<Object[]> categoryCounts = shoppingListRepository.countShoppingListsByCategoryForUser(clientName);
        Map<String, Long> countsMap = new HashMap<>();
        if (categoryCounts != null && !categoryCounts.isEmpty()) {
            for (Object[] result : categoryCounts) {
                ListCategory category = (ListCategory) result[0];
                Long count = (Long) result[1];
                countsMap.put(category.toString(), count);}

        }
        return countsMap;
    }

    @Override
    public ShoppingList getShoppingListById(Long listId) {
        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(listId);
        if (shoppingListOptional.isPresent()) {
            ShoppingList shoppingList = shoppingListOptional.get();
            Optional<Client> userOptional = Optional.ofNullable(shoppingList.getClient());
            userOptional.ifPresent(user -> {
                ClientDTO clientDTO = user.toDTO();
                shoppingList.setClientDTO(clientDTO);
            });
            return shoppingList;
        } else {
            return null;
        }
    }


    @Transactional
    @Override
    public ShoppingList updateShoppingList(Long listId, ShoppingList shoppingList) {
        ShoppingList existingList = shoppingListRepository.findById(listId).orElse(null);
        if (existingList != null) {
            // Keep the existing items
            List<Item> existingItems = existingList.getItems();

            // Update the name and category
            existingList.setName(shoppingList.getName());
            existingList.setCategory(shoppingList.getCategory());

            // Re-assign the existing items to the updated ShoppingList
            existingList.setItems(existingItems);

            // Save the updated ShoppingList
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
    public List<ShoppingList> getShoppingListsByClientId(Long userId) {
        List<ShoppingList> shoppingLists = shoppingListRepository.findByClientId(userId);
        // Loop through the list and convert User to UserDTO
        shoppingLists.forEach(shoppingList -> {
            Client client = shoppingList.getClient();
            if (client != null) {
                ClientDTO clientDTO = client.toDTO();
                shoppingList.setClientDTO(clientDTO);
            }
        });
        //using stream
        // Use stream to convert User to UserDTO for each ShoppingList
//        shoppingLists.stream()
//                .map(ShoppingList::getUser) // Get the User for each ShoppingList
//                .filter(Objects::nonNull) // Filter out null Users
//                .map(User::toDTO) // Convert User to UserDTO
//                .forEach(userDTO -> {
//                    shoppingLists.forEach(shoppingList -> {
//                        if (shoppingList.getUser() != null && shoppingList.getUser().getId().equals(userDTO.getId())) {
//                            shoppingList.setUserDTO(userDTO); // Set UserDTO for the matching ShoppingList
//                        }
//                    });
//                });

        return shoppingLists;
    }


}

