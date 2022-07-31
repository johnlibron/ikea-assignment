package com.ikea.assignment;

import com.ikea.assignment.data.dao.InventoryRepository;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHasAvailableProducts() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/products/available"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        JSONArray items = new JSONArray(response.getContentAsString());
        assertTrue(items.length() > 0);
    }

    @Test
    public void testNoAvailableProducts() throws Exception {
        inventoryRepository.findAll().forEach(inventory -> {
            inventory.setStock(0L);
            inventoryRepository.save(inventory);
        });
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/products/available"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        JSONArray items = new JSONArray(response.getContentAsString());
        assertEquals(items.length(), 0);
    }

    @Test
    public void testPurchaseProduct() throws Exception {
        String productName = "Dining Chair";
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/v1/products/" + productName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(response.getStatus(), 200);
    }

    @Test
    public void testPurchaseWrongProduct() throws Exception {
        String productName = "Cabinet";
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/v1/products/" + productName))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertEquals(response.getStatus(), 404);
    }

    @Test
    public void testPurchaseProductWithNoInventory() throws Exception {
        inventoryRepository.deleteAll();
        String productName = "Dining Chair";
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/v1/products/" + productName))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertEquals(response.getStatus(), 404);
    }

    @Test
    public void testPurchaseProductWithInsufficientInventory() throws Exception {
        inventoryRepository.findAll().forEach(inventory -> {
            inventory.setStock(0L);
            inventoryRepository.save(inventory);
        });
        String productName = "Dining Chair";
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/v1/products/" + productName))
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn().getResponse();
        assertEquals(response.getStatus(), 409);
    }
}
