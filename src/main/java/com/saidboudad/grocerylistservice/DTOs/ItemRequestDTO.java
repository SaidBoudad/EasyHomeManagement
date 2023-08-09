package com.saidboudad.grocerylistservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemRequestDTO {
    private String name;
    private Category category;
    private double quantity;
    private Long shoppingListId;
    private Long userId;


}
