package com.saidboudad.grocerylistservice;

import com.saidboudad.grocerylistservice.entity.ShoppingList;
import com.saidboudad.grocerylistservice.repository.ShoppingListRepository;
import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @InjectMocks
    private ShoppingListServiceImpl shoppingListService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateShoppingList() {
        // Test data
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName("My Shopping List");

        // Mock repository behavior
        when(shoppingListRepository.save(any(ShoppingList.class))).thenReturn(shoppingList);

        // Call the service method
        ShoppingList createdList = shoppingListService.createShoppingList(shoppingList);

        // Assertions
        assertNotNull(createdList);
        assertEquals("My Shopping List", createdList.getName());

    }


}
