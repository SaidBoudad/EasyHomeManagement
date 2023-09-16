package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.DTOs.ClientDTO;
import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.exceptions.DuplicateEmailException;
import com.saidboudad.grocerylistservice.exceptions.DuplicateUsernameException;
import com.saidboudad.grocerylistservice.service.client.ClientService;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class ClientController {

    private final ClientService clientService;
    private final ShoppingListService shoppingListService;

    public ClientController(ClientService clientService, ShoppingListService shoppingListService) {
        this.clientService = clientService;
        this.shoppingListService = shoppingListService;
    }

    // Display the user creation form
    @GetMapping("/user/create")
    @PreAuthorize("hasRole('USER')")
    public String showCreateUserForm(Model model) {
        model.addAttribute("client", new Client());
        return "create-user";
    }

    // Endpoint to create a new user
    @PostMapping("/user/save")
    @PreAuthorize("hasRole('USER')")
    public String createUser(Model model, @Valid Client client,
                             @RequestParam("confirmPass") String confirmPass,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors())return "create-user";
        try {
            Client createdClient = clientService.createClient(client,confirmPass);
            model.addAttribute("createdClientDTO", createdClient.toDTO());
            model.addAttribute("successMessage", "User created successfully!");
        } catch (DuplicateEmailException e) {
            model.addAttribute("errorMessage", "Email already exists.");
        } catch (DuplicateUsernameException e) {
            model.addAttribute("errorMessage", "Username already exists.");
        }
          return "create-user"; //to keep the new user in some page and successMessage
//        return "redirect:/users?keyword="+user.getClientName(); // to redirect admin to the list of clients
    }

    //Get edit page
    @GetMapping("/admin/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String getEditClientPage( Model model, Long id,String keyword,int page){
        Client client = clientService.getClientById(id);
        if (client == null) throw new RuntimeException("User Doesn't Exist");
        model.addAttribute("client", client);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "edit-user";
    }

    //Endpoint to update a user
    @PostMapping("/admin/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateClient(Model model, @Valid Client editedClient,
                               @RequestParam("confirmPass") String confirmPass,
                               BindingResult bindingResult,
                               @RequestParam(defaultValue = "") String keyword,
                               @RequestParam(defaultValue = "0") int page) {
        if(bindingResult.hasErrors())
            return "edit-user"; // Return the edit form with errors
        try {
            // Retrieve the existing user by ID
            Client existingClient = clientService.getClientById(editedClient.getId());
            // Update the existing client's with the edited client
            clientService.updateClient(existingClient.getId(), editedClient, confirmPass);
            // Success message
            model.addAttribute("successMessage", "User updated successfully!");

        } catch (DuplicateEmailException e) {
            model.addAttribute("errorMessage", "Email already exists.");
//            return "edit-user"; // Redirect back to the edit form with error message
        } catch (DuplicateUsernameException e) {
            model.addAttribute("errorMessage", "Username already exists.");
//            return "edit-user"; // Redirect back to the edit form with error message
        }
        return "create-user";
        // Redirect back to the user list page
//        return "redirect:/users/admin?page="+page+"&keyword="+keyword;
    }

    //method handler to handel list of users and return model and view
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAllClients(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "6") int size,
                            @RequestParam(name = "keyword", defaultValue = "") String keyword,
                            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder) {
        Page<Client> usersPage = clientService.getClientsByPage(keyword, page, size,sortOrder);
        model.addAttribute("usersPage", usersPage.getContent());
        model.addAttribute("pages",new int[usersPage.getTotalPages()]);//create table to hold the pages
        model.addAttribute("setPage",page);
        model.addAttribute("keyword",keyword);
        model.addAttribute("currentPage",page);
        return "users";
    }

    //I'm using HTML and Thymeleaf. Since HTML forms and Thymeleaf templates don't inherently support HTTP DELETE and PUT methods,
    //I've employed POST and GET mapping requests to handle these requests
    //Delete
    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteClientById(Long id,String keyword,int page){
        clientService.deleteClientById(id);
        return "redirect:/users/admin?page="+page+"&keyword="+keyword;
    }











    //    This part is for the RestFull calls
    // Endpoint to get a specific user by ID Restful calls
    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<ClientDTO> getUserById(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            ClientDTO clientDTO = client.toDTO();
            return ResponseEntity.ok(clientDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update an existing user
    @PutMapping("/{clientId}")
    @ResponseBody
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long clientId,
                                                  @RequestBody Client client,
                                                  @RequestParam String confirmPass) {
        Client updatedClient = clientService.updateClient(clientId, client, confirmPass);
        if (updatedClient != null) {
            ClientDTO updatedClientDTO = updatedClient.toDTO();
            return ResponseEntity.ok(updatedClientDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{clientId}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long clientId) {
        boolean deleted = clientService.deleteClientById(clientId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to get shopping lists for a specific user by ID
    @GetMapping("/{clientId}/lists")
    @ResponseBody
    public ResponseEntity<List<ShoppingList>> getShoppingListsForUser(@PathVariable Long clientId) {
        List<ShoppingList> shoppingLists = shoppingListService.getShoppingListsByClientId(clientId);
        return ResponseEntity.ok(shoppingLists);
    }


}
