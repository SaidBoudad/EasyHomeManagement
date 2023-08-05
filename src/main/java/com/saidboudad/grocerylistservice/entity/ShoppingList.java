package com.saidboudad.grocerylistservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saidboudad.grocerylistservice.DTOs.UserDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shopping_list_seq")
    @SequenceGenerator(name = "shopping_list_seq", sequenceName = "shopping_list_seq", initialValue = 100, allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="numberOfItems")
    private int numberOfItem;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // foreign key to the User entity
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "shoppingList", targetEntity = Item.class)
    private List<Item> items = new ArrayList<>(); // Initialize the items list

    @Column(name = "date_created", updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    @UpdateTimestamp
    private LocalDateTime dateModified;

    //Will not be persisted in the database. It will only be available during the lifetime of the ShoppingList object.
    @Transient
    private String lastChange;

    @Transient
    private UserDTO userDTO;

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @PreUpdate
    @PreRemove
    private void updateNumberOfItems() {
        if (items != null) {
            numberOfItem = items.size();
        }
    }

}

