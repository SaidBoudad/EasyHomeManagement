//package com.saidboudad.grocerylistservice;
//
//import com.saidboudad.grocerylistservice.controller.ShoppingListController;
//import com.saidboudad.grocerylistservice.entity.ShoppingList;
//import com.saidboudad.grocerylistservice.service.shppinglistService.ShoppingListService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilders;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class ShoppingListControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private ShoppingListService shoppingListService;
//
//    @InjectMocks
//    private ShoppingListController shoppingListController;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(shoppingListController).build();
//    }
//
//    @Test
//    public void testGetShoppingListById() throws Exception {
//        // Test data
//        Long listId = 1L;
//        ShoppingList shoppingList = new ShoppingList();
//        shoppingList.setId(listId);
//        shoppingList.setName("My Shopping List");
//
//        // Mock service behavior
//        when(shoppingListService.getShoppingListById(listId)).thenReturn(shoppingList);
//
//        // Perform the GET request to "/shopping-lists/1" and assert the response
//        mockMvc.perform(get("/shopping-lists/{listId}", listId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(listId))
//                .andExpect(jsonPath("$.name").value("My Shopping List"));
//    }
//
//}
//
