package com.saidboudad.grocerylistservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "items")
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "quantity",nullable = false)
    private double quantity;


    @Column(name = "date_created",updatable = false)
    @CreationTimestamp
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private ShoppingList shoppingList;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // foreign key to the User entity
    @JsonIgnore
    private User user;
}
enum Category {
    CUISINE,
    HEALTH,
    CLEANING,
    CHILDREN,
    FRUIT,

}