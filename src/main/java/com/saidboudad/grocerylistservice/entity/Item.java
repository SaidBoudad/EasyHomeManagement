package com.saidboudad.grocerylistservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.saidboudad.grocerylistservice.DTOs.ItemCategory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ItemCategory itemCategory;

    @Column(name = "quantity", nullable = false)
    private double quantity;


    @Column(name = "date_created", updatable = false)
    @CreationTimestamp
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    @JsonBackReference // Do not serialize this property (ShoppingList) to avoid recursion
    @ToString.Exclude // Exclude shoppingList field from toString
    private ShoppingList shoppingList;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false) // foreign key to the User entity
    @JsonBackReference // Do not serialize this property
    @ToString.Exclude // Exclude client field from toString
    private Client client;
}
