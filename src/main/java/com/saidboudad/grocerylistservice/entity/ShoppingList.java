package com.saidboudad.grocerylistservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class ShoppingList {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "quantity", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // foreign key to the User entity
    private User user;

    @OneToMany(mappedBy = "shoppingList", targetEntity = Item.class)
    private List<Item> items;

    @Column(name = "date_created", updatable = false)
    @CreationTimestamp
    private LocalDate dateCreated;

    @Column(name = "date_modified", updatable = false)
    @CreationTimestamp
    private LocalDate dateModified;
}

