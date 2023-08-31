package com.saidboudad.grocerylistservice.service.shppinglistService;

import com.saidboudad.grocerylistservice.DTOs.ClientDTO;
import com.saidboudad.grocerylistservice.DTOs.ListCategory;
import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.exceptions.UserNotFoundException;
import com.saidboudad.grocerylistservice.repository.ClientRepository;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

        for (Object[] result : categoryCounts) {
            ListCategory category = (ListCategory) result[0];
            Long count = (Long) result[1];
            countsMap.put(category.toString(), count);// Convert enum to String the key of hashmap should be immutable
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
            List<String> changes = new ArrayList<>();

            if (!existingList.getName().equals(shoppingList.getName())) {
                changes.add("Name changed from '" + existingList.getName() + "' to '" + shoppingList.getName() + "'");
                existingList.setName(shoppingList.getName());
            }
            if (existingList.getNumberOfItem() != shoppingList.getNumberOfItem()) {
                changes.add("Number of items changed from '" + existingList.getNumberOfItem() + "' to '" + shoppingList.getNumberOfItem() + "'");
                existingList.setNumberOfItem(shoppingList.getNumberOfItem());
            }
            if (!existingList.getItems().equals(shoppingList.getItems())) {
                changes.add("Items changed from " + existingList.getItems() + " to " + shoppingList.getItems());
                existingList.setItems(shoppingList.getItems());
            }

            // Add more checks for other fields I want to track and update them accordingly.

            if (!changes.isEmpty()) {
                // Fetch the associated User entity and convert it to UserDTO
                Client client = existingList.getClient();
                ClientDTO clientDTO = client.toDTO();

                // Create a message containing the list of changes and the modification time
                String modificationMessage = String.join(", ", changes);
                existingList.setClientDTO(clientDTO);
                existingList.setLastChange(modificationMessage);

                // Save the changes to the database
                return shoppingListRepository.save(existingList);
            }
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

