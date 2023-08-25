package com.saidboudad.grocerylistservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saidboudad.grocerylistservice.DTOs.ClientDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clientName", nullable = false, unique = true)
    @NotEmpty @NotBlank
    @Size(min=2 ,max=40)
    private String clientName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String confirmPass;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Item> items;


    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ShoppingList> shoppingLists;


    //code before removing : cascade = CascadeType.ALL, orphanRemoval = true , to solve the error
    //"HibernateException: A collection with cascade="all-delete-orphan"
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<Item> items;
//
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<ShoppingList> shoppingLists;

    public ClientDTO toDTO() {
        ClientDTO dto = new ClientDTO();
        dto.setId(this.id);
        dto.setClientName(this.clientName);
        dto.setEmail(this.email);

        return dto;
    }
}
