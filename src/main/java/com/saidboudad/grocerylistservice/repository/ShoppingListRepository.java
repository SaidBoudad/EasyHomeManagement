package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.DTOs.ListCategory;
import com.saidboudad.grocerylistservice.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    @Query("SELECT sl FROM ShoppingList sl WHERE sl.client.id = :clientId")
    List<ShoppingList> findByClientId(Long clientId);

    List<ShoppingList> findByCategoryAndClient_ClientName(ListCategory category,String clientName);

    @Query("SELECT s.category, COUNT(s) FROM ShoppingList s WHERE s.client.clientName = :clientName GROUP BY s.category")
    List<Object[]> countShoppingListsByCategoryForUser(@Param("clientName") String clientName);


}

