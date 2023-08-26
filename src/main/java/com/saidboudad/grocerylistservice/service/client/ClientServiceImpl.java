package com.saidboudad.grocerylistservice.service.client;

import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.exceptions.DuplicateEmailException;
import com.saidboudad.grocerylistservice.exceptions.DuplicateUsernameException;
import com.saidboudad.grocerylistservice.repository.ClientRepository;
import com.saidboudad.grocerylistservice.security.Service.AccountService;
import com.saidboudad.grocerylistservice.security.entities.AppUser;
import com.saidboudad.grocerylistservice.security.repo.AppUserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepo appUserRepo;


    public ClientServiceImpl(ClientRepository clientRepository, AccountService accountService, PasswordEncoder passwordEncoder,
                             AppUserRepo appUserRepo) {
        this.clientRepository = clientRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.appUserRepo = appUserRepo;
    }

    @Override
    public Client createClient(Client client,String confirmPass) {
        // Check for duplicate email
        if (clientRepository.findByEmail(client.getEmail()) != null) {
            throw new DuplicateEmailException(client.getEmail());
        }

        // Check for duplicate username
        if (clientRepository.findByClientName(client.getClientName()) != null) {
            throw new DuplicateUsernameException(client.getClientName());
        }

        // Check if password and confirmPass match
        if (!client.getPassword().equals(confirmPass)) {
            throw new RuntimeException("Passwords do not match");
        }

        // Encode the password
        String encodedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);

        Client savedClient = clientRepository.save(client);

        // Create corresponding AppUser entry
        accountService.addNewUser(savedClient.getClientName()
                , savedClient.getPassword()
                , savedClient.getEmail());

        return savedClient;
    }

    @Override
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId).orElse(null);

    }

    @Override
    public Client updateClient(Long clientId, Client updatedClient,String confirmPass) {
        Client existingClient = clientRepository.findById(clientId).orElse(null);
        if (existingClient != null) {
            // Check if the new email is already used by another user
            Client clientWithNewEmail = clientRepository.findByEmail(updatedClient.getEmail());
            if (clientWithNewEmail != null && !clientWithNewEmail.getId().equals(clientId)) {
                throw new DuplicateEmailException("Email is already in use by another user");
            }

            // Check if the new clientName is already used by another client
            Client clientWithNewUsername = clientRepository.findByClientName(updatedClient.getClientName());
            if (clientWithNewUsername != null && !clientWithNewUsername.getId().equals(clientId)) {
                throw new DuplicateUsernameException("Username is already in use by another user");
            }

            existingClient.setClientName(updatedClient.getClientName());
            existingClient.setEmail(updatedClient.getEmail());

            // Only update the password if a new password is provided
            if (!updatedClient.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(updatedClient.getPassword());
                existingClient.setPassword(encodedPassword);
            }

            // Update the corresponding AppUser entry
            AppUser appUser = accountService.loadUserByUsername(existingClient.getClientName());
            if (appUser != null) {
                appUser.setUsername(existingClient.getClientName());
                appUser.setEmail(existingClient.getEmail());
                appUser.setPassword(existingClient.getPassword());
                appUserRepo.save(appUser);
            }

            return clientRepository.save(existingClient);
        }
        return null;
    }

    @Override
    public boolean deleteClientById(Long clientId) {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
            return true;
        }
        return false;
    }

    // Method to get all shopping lists for a specific user by ID
    @Override
    public List<ShoppingList> getShoppingListsByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null) {
            return client.getShoppingLists();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    public Page<Client> getClientsByPage(String keyword , int page, int size, String sortOrder) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), "id"); // Create Sort object
        Pageable pageable = PageRequest.of(page, size, sort); // Apply sort to pageable
        return clientRepository.findByClientNameContains(keyword,pageable);

    }

    @Override
    public Client findByUsername(String username) {
        return clientRepository.findByClientName(username);
    }


}
