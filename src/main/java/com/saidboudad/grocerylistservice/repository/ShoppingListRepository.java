package com.saidboudad.grocerylistservice.repository;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {


}
