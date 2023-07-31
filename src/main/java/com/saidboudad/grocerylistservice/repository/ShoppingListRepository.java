package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    @Query("SELECT sl FROM ShoppingList sl WHERE sl.user.id = :userId")
    List<ShoppingList> findByUserId(Long userId);


}
