package com.saidboudad.grocerylistservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingListRequest {
    private String name;
    private Long userId;
}
