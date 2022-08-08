package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dao.ArticleRepository;
import com.ikea.warehouseapp.data.dao.InventoryRepository;
import com.ikea.warehouseapp.data.dao.ProductRepository;
import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.dto.ProductIncomingDto;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Inventory;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final ProductRepository productRepository;

    private final ArticleRepository articleRepository;

    private final InventoryRepository inventoryRepository;

    @Override
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

    @Override
    public Long getAvailableInventory(List<Article> articles) {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        long minQuantity = 0;
        for (Article article : articles) {
            Optional<Long> optionalStock = inventoryList.stream()
                    .filter(e -> e.getId().equals(article.getInventoryId()))
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

    @Override
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }


    @Transactional
    @Override
    public void purchaseProduct(Product product) {
        List<Integer> inventoryIds = new ArrayList<>();
        List<Inventory> inventories = new ArrayList<>();
        for (Article article : product.getArticles()) {
            Optional<Inventory> optionalInventory = inventoryRepository.findById(article.getInventoryId());
            optionalInventory.ifPresent(inventory -> {
                inventory.setStock(inventory.getStock() - article.getAmountOf());
                inventoryRepository.save(inventory);
            });
        }
        log.info(INVENTORY_UPDATED_LOG, inventoryIds);
    }

    @Override
    public List<Article> getProductArticles(ProductIncomingDto productIncomingDto) {
        List<Article> articles = new ArrayList<>();
        if (productIncomingDto.getArticles().size() > 0) {
            Product product = new Product();
            product.setName(productIncomingDto.getName());
            product.setPrice(productIncomingDto.getPrice());
            for (ArticleDto articleDto : productIncomingDto.getArticles()) {
                Optional<Inventory> optionalInventory = inventoryRepository.findByName(articleDto.getName());
                if (optionalInventory.isEmpty()) {
                    return null;
                }
//                articles.add(new Article(
//                        articleDto.getAmountOf(),
//                        optionalInventory.get().getId(),
//                        product
//                ));
            }
        }
        return articles;
    }


    @Transactional
    @Override
    public ProductDto addProduct(ProductIncomingDto productIncomingDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productIncomingDto, product);
        List<Article> articles = new ArrayList<>();
        for (ArticleDto articleDto : productIncomingDto.getArticles()) {
            Article article = new Article();
            BeanUtils.copyProperties(articleDto, article);
            articles.add(article);
        }
        product.setArticles(articles);
        log.info("product: " + product);
        return null;
    }
}
