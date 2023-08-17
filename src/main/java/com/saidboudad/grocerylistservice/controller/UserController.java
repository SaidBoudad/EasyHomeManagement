package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.entity.User;
import com.saidboudad.grocerylistservice.exceptions.DuplicateEmailException;
import com.saidboudad.grocerylistservice.exceptions.DuplicateUsernameException;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
import com.saidboudad.grocerylistservice.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ShoppingListService shoppingListService;

    public UserController(UserService userService, ShoppingListService shoppingListService) {
        this.userService = userService;
        this.shoppingListService = shoppingListService;
    }

    //Get home page
    @GetMapping("/home")
    public String home(Model model) {
        return "home-page";
    }

    // Display the user creation form
    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user"; // Return the view name for the form page
    }

    // Endpoint to create a new user
    @PostMapping
    public String createUser(@ModelAttribute User user, Model model) {
        try {
            User createdUser = userService.createUser(user);
            model.addAttribute("createdUserDTO", createdUser.toDTO());
            model.addAttribute("successMessage", "User created successfully!");
        } catch (DuplicateEmailException e) {
            model.addAttribute("errorMessage", "Email already exists.");
        } catch (DuplicateUsernameException e) {
            model.addAttribute("errorMessage", "Username already exists.");
        }
        return "create-user";
    }

    //method handler to handel lists students and return model and view
    @GetMapping
    public String listAllUsers(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "5") int size,
                            @RequestParam(name = "keyword", defaultValue = "") String keyword  ) {
        Page<User> usersPage = userService.getUsersByPage(keyword, page, size);
        model.addAttribute("usersPage", usersPage.getContent());
        model.addAttribute("pages",new int[usersPage.getTotalPages()]);
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
    public String getEditeUser(@PathVariable Long id,Model model){
        model.addAttribute("user",userService.getUserById(id));
        return "edit_student";
    }
    //Update
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,@ModelAttribute("user") User user,Model model){
        //get user from DB by id
        User existingUser = userService.getUserById(id);
        existingUser.setId(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        //save updated user object
        userService.createUser(existingUser);
        return "redirect:/users";
    }







    // Endpoint to get a specific user by ID
//    @GetMapping("/{userId}")
//    @ResponseBody
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
//        User user = userService.getUserById(userId);
//        if (user != null) {
//            UserDTO userDTO = user.toDTO();
//            return ResponseEntity.ok(userDTO);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Endpoint to update an existing user
//    @PutMapping("/{userId}")
//    @ResponseBody
//    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody User user) {
//        User updatedUser = userService.updateUser(userId, user);
//        if (updatedUser != null) {
//            UserDTO updatedUserDTO = updatedUser.toDTO();
//            return ResponseEntity.ok(updatedUserDTO);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Endpoint to delete a user by ID
//    @DeleteMapping("/{userId}")
//    @ResponseBody
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        boolean deleted = userService.deleteUserById(userId);
//        if (deleted) {
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Endpoint to get shopping lists for a specific user by ID
//    @GetMapping("/{userId}/lists")
//    @ResponseBody
//    public ResponseEntity<List<ShoppingList>> getShoppingListsForUser(@PathVariable Long userId) {
//        List<ShoppingList> shoppingLists = shoppingListService.getShoppingListsByUserId(userId);
//        return ResponseEntity.ok(shoppingLists);
//    }


}
