package com.saidboudad.grocerylistservice.service.client;

import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {

    Client createClient(Client client,String confirmPass);
    Client getClientById(Long clientId);
    Client updateClient(Long clientId, Client client,String confirmPass);
    boolean deleteClientById(Long clientId);
    List<ShoppingList> getShoppingListsByClientId(Long clientId);
    List<Client> getAllClients();
    Page<Client> getClientsByPage(String keyword, int page, int size, String sortOrder);
    Client findByUsername(String username);
    Client findByClientNameAndEmail(String clientName, String email);
    void updateClientPassword(Client client, String newPassword);
}
