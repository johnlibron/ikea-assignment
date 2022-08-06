package com.ikea.warehouseapp;

import com.ikea.warehouseapp.data.dao.InventoryRepository;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    public ProductService productService;

    @Test
    public void testGetAvailableProducts() {
        List<AvailableProductDto> availableProducts = productService.getAvailableProducts();
        List<AvailableProductDto> mockAvailableProducts = Arrays.asList(
            new AvailableProductDto("Dining Chair", 2L),
            new AvailableProductDto("Dining Table", 1L)
        );
        assertEquals(mockAvailableProducts, availableProducts);
    }

    @Test
    public void testNoAvailableProducts() {
        inventoryRepository.findAll().forEach(inventory -> {
            inventory.setStock(0L);
            inventoryRepository.save(inventory);
        });
        List<AvailableProductDto> availableProducts = productService.getAvailableProducts();
        assertEquals(0, availableProducts.size());
    }

    @Test
    public void testGetAvailableInventory() {
        List<Article> mockArticles = Arrays.asList(
            new Article(4L, 1L),
            new Article(8L, 2L),
            new Article(1L, 3L)
        );
        Long mockAvailableInventory = productService.getAvailableInventory(mockArticles);
        assertTrue(mockAvailableInventory > 0);
    }

    @Test
    public void testGetAvailableInventoryWithInsufficientInventory() {
        inventoryRepository.findAll().forEach(inventory -> {
            inventory.setStock(0L);
            inventoryRepository.save(inventory);
        });
        List<Article> mockArticles = Arrays.asList(
            new Article(4L, 1L),
            new Article(8L, 2L),
            new Article(1L, 3L)
        );
        Long mockAvailableInventory = productService.getAvailableInventory(mockArticles);
        assertEquals(0L, mockAvailableInventory);
    }

    @Test
    public void testGetAvailableInventoryWithNoInventoryFound() {
        inventoryRepository.deleteAll();
        List<Article> mockArticles = Arrays.asList(
            new Article(4L, 1L),
            new Article(8L, 2L),
            new Article(1L, 3L)
        );
        Long mockAvailableInventory = productService.getAvailableInventory(mockArticles);
        assertNull(mockAvailableInventory);
    }

    @Test
    public void testGetProduct() {
        Optional<Product> optionalProduct = productService.getProductByName("Dining Chair");
        assertTrue(optionalProduct.isPresent());
    }

    @Test
    public void testGetWrongProduct() {
        Optional<Product> optionalProduct = productService.getProductByName("Cabinet");
        assertFalse(optionalProduct.isPresent());
    }

    @Test
    public void testAvailableInventoryIfPurchaseProduct() {
        Optional<Product> optionalProduct = productService.getProductByName("Dining Chair");
        assertTrue(optionalProduct.isPresent());
        productService.purchaseProduct(optionalProduct.get());
        Long availableInventory = productService.getAvailableInventory(optionalProduct.get().getArticles());
        assertEquals(1L, availableInventory);
    }
}
