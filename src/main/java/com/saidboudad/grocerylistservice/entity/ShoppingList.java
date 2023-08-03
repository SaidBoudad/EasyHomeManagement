package com.saidboudad.grocerylistservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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
    private List<Item> items;

    @Column(name = "date_created", updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_modified", updatable = false)
    @UpdateTimestamp
    private LocalDateTime dateModified;


    @PreUpdate
    @PreRemove
    private void updateNumberOfItems() {
        numberOfItem = items.size(); // Assuming you have a List<Item> items field
    }
}

