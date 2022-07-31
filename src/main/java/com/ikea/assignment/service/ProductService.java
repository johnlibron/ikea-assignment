package com.ikea.assignment.service;

import com.ikea.assignment.controller.ProductController;
import com.ikea.assignment.data.dao.InventoryRepository;
import com.ikea.assignment.data.dao.ProductRepository;
import com.ikea.assignment.data.dto.AvailableProductDto;
import com.ikea.assignment.data.model.Article;
import com.ikea.assignment.data.model.Inventory;
import com.ikea.assignment.data.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public List<AvailableProductDto> getAvailableProducts() {
        List<AvailableProductDto> availableProducts = new ArrayList<>();
        productRepository.findAll().forEach(product -> {
            long quantity = getAvailableInventory(product.getArticles());
            if (quantity > 0) {
                availableProducts.add(new AvailableProductDto(product.getName(), quantity));
            }
        });
        return availableProducts;
    }

    public Long getAvailableInventory(List<Article> articles) {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        long minQuantity = 0;
        for (Article article : articles) {
            Optional<Long> optionalStock = inventoryList.stream()
                    .filter(e -> e.getArticleId().equals(article.getArticleId()))
                    .findFirst().map(Inventory::getStock);
            if (optionalStock.isEmpty()) {
                return null;
            }
            if (optionalStock.get() < article.getAmountOf()) {
                minQuantity = 0;
                break;
            }
            long quantityNeeded = optionalStock.get() / article.getAmountOf();
            if (minQuantity == 0) {
                minQuantity = quantityNeeded;
            } else {
                minQuantity = Math.min(minQuantity, quantityNeeded);
            }
        }
        return minQuantity;
    }

    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Transactional
    public void purchaseProduct(Product product) {
        for (Article article : product.getArticles()) {
            Optional<Inventory> optionalInventory = inventoryRepository.findById(article.getArticleId());
            optionalInventory.ifPresent(inventory -> {
                inventory.setStock(inventory.getStock() - article.getAmountOf());
                inventoryRepository.save(inventory);
                logger.info(INVENTORY_UPDATED_LOG, inventory);
            });
        }
    }
}
