package com.saidboudad.grocerylistservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidboudad.grocerylistservice.DTOs.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Item> items;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ShoppingList> shoppingLists;

    public UserDTO toDTO() {
        UserDTO dto = new UserDTO();
        dto.setId(this.id);
        dto.setUsername(this.username);
        dto.setEmail(this.email);

        return dto;
    }
}
