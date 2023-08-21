package com.saidboudad.grocerylistservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.saidboudad.grocerylistservice.DTOs.ClientDTO;
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
    @SequenceGenerator(name = "shopping_list_seq", sequenceName = "shopping_list_seq", initialValue = 100)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="numberOfItems",nullable = false)
    private int numberOfItem;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false) // foreign key to the User entity
    @JsonBackReference
    private Client client;

    @OneToMany(mappedBy = "shoppingList",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Serialize this property (ShoppingList) as a forward reference
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
    private ClientDTO clientDTO;

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }

    @PrePersist
    @PreUpdate
    @PreRemove
    private void updateNumberOfItems() {
        if (items != null) {
            numberOfItem = items.size();
        }
    }

}

