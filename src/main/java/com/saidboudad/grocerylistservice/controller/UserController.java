package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.DTOs.UserDTO;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.exceptions.DuplicateEmailException;
import com.saidboudad.grocerylistservice.exceptions.DuplicateUsernameException;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import com.saidboudad.grocerylistservice.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ShoppingListService shoppingListService;

    public UserController(UserService userService, ShoppingListService shoppingListService) {
        this.userService = userService;
        this.shoppingListService = shoppingListService;
    }

    // Display the user creation form
    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user"; // Return the view name for the form page
    }

    // Endpoint to create a new user
    @PostMapping("/save")
    public String createUser( Model model, @Valid User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors())return "create-user";
        try {
            User createdUser = userService.createUser(user);
            model.addAttribute("createdUserDTO", createdUser.toDTO());
            model.addAttribute("successMessage", "User created successfully!");
        } catch (DuplicateEmailException e) {
            model.addAttribute("errorMessage", "Email already exists.");
        } catch (DuplicateUsernameException e) {
            model.addAttribute("errorMessage", "Username already exists.");
        }
//        return "create-user"; to make the new user in the some page and successMessage
        return "redirect:/users?keyword="+user.getUsername();
    }

    //Endpoint to update a user
    @PostMapping("/update")
    public String updateUser( Model model, @Valid User editedUser, BindingResult bindingResult,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page) {
        if(bindingResult.hasErrors())return "edit-user"; // Return the edit form with errors
        try {
            // Retrieve the existing user by ID
            User existingUser = userService.getUserById(editedUser.getId());
            // Update the existing user's properties with the edited data
            existingUser.setUsername(editedUser.getUsername());
            existingUser.setEmail(editedUser.getEmail());
            existingUser.setPassword(editedUser.getPassword());
            // Save the changes to the existing user
            userService.updateUser(existingUser.getId(), existingUser);
            model.addAttribute("successMessage", "User updated successfully!");

        } catch (DuplicateEmailException e) {
            model.addAttribute("errorMessage", "Email already exists.");
        } catch (DuplicateUsernameException e) {
            model.addAttribute("errorMessage", "Username already exists.");
        }

        // Redirect back to the user list page
        return "redirect:/users?page="+page+"&keyword="+keyword;
    }

    //method handler to handel list of users and return model and view
    @GetMapping
    public String listAllUsers(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "3") int size,
                            @RequestParam(name = "keyword", defaultValue = "") String keyword,
                            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder) {
        Page<User> usersPage = userService.getUsersByPage(keyword, page, size,sortOrder);
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
    @GetMapping("/delete")
    public String deleteUserById(Long id,String keyword,int page){
        userService.deleteUserById(id);
        return "redirect:/users?page="+page+"&keyword="+keyword;
    }
    //Get edit page
    @GetMapping("/edit")
    public String getEditeUser( Model model, Long id,String keyword,int page){
        User user = userService.getUserById(id);
        if (user == null) throw new RuntimeException("User Doesn't Exist");
        model.addAttribute("user",user);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "edit-user";
    }







    // Endpoint to get a specific user by ID Restful calls
    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            UserDTO userDTO = user.toDTO();
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update an existing user
    @PutMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        if (updatedUser != null) {
            UserDTO updatedUserDTO = updatedUser.toDTO();
            return ResponseEntity.ok(updatedUserDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to delete a user by ID
    @DeleteMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to get shopping lists for a specific user by ID
    @GetMapping("/{userId}/lists")
    @ResponseBody
    public ResponseEntity<List<ShoppingList>> getShoppingListsForUser(@PathVariable Long userId) {
        List<ShoppingList> shoppingLists = shoppingListService.getShoppingListsByUserId(userId);
        return ResponseEntity.ok(shoppingLists);
    }


}
