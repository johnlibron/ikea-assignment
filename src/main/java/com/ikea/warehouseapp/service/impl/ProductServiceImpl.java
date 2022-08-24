package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dao.InventoryRepository;
import com.ikea.warehouseapp.data.dao.ProductRepository;
import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.dto.ProductIncomingDto;
import com.ikea.warehouseapp.data.model.Inventory;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.service.InventoryService;
import com.ikea.warehouseapp.service.JsonParserService;
import com.ikea.warehouseapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final ProductRepository productRepository;

    private final JsonParserService jsonParserService;

    private final InventoryService inventoryService;

    private final InventoryRepository inventoryRepository;

    @Override
    public List<AvailableProductDto> getAvailableProducts() {
        // TODO - Minimize db transactions, check db caching, use hashmap
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
    public Long getAvailableInventory(List<ArticleDto> articles) {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        long minQuantity = 0;
        for (ArticleDto article : articles) {
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

    @Override
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }


    @Transactional
    @Override
    public void purchaseProduct(Product product) {
        List<Integer> inventoryIds = new ArrayList<>();
        List<Inventory> inventories = new ArrayList<>();
        for (ArticleDto article : product.getArticles()) {
            Optional<Inventory> optionalInventory = inventoryRepository.findByArticleId(article.getArticleId());
            optionalInventory.ifPresent(inventory -> {
                inventory.setStock(inventory.getStock() - article.getAmountOf());
                inventoryRepository.save(inventory);
            });
        }
        log.info(INVENTORY_UPDATED_LOG, inventoryIds);
    }

    @Override
    public List<ArticleDto> getProductArticles(ProductIncomingDto productIncomingDto) {
        List<ArticleDto> articles = new ArrayList<>();
        if (productIncomingDto.getArticles().size() > 0) {
            Product product = new Product();
            product.setName(productIncomingDto.getName());
            product.setPrice(productIncomingDto.getPrice());
            for (ArticleDto articleDto : productIncomingDto.getArticles()) {
                // TODO
//                Optional<Inventory> optionalInventory = inventoryRepository.findByName(articleDto.getName());
                Optional<Inventory> optionalInventory = Optional.empty();
                if (optionalInventory.isEmpty()) {
                    return null;
                }
                articles.add(new ArticleDto(
                    optionalInventory.get().getArticleId(),
                    articleDto.getAmountOf()
                ));
            }
        }
        return articles;
    }


    @Transactional
    @Override
    public ProductDto addProduct(ProductIncomingDto productIncomingDto) {
        // TODO
        Product product = new Product();
        BeanUtils.copyProperties(productIncomingDto, product);
        List<ArticleDto> articles = new ArrayList<>();
        for (ArticleDto articleDto : productIncomingDto.getArticles()) {
            ArticleDto article = new ArticleDto();
            BeanUtils.copyProperties(articleDto, article);
            articles.add(article);
        }
        product.setArticles(articles);
        log.info("product: " + product);
        return null;
    }

    @Transactional
    @Override
    public void importProducts(String pathname) throws IOException {
        // TODO: Add batch insert support, check deadlock scenario, and add logs
        List<Product> products = jsonParserService.getProducts(pathname);
        Set<String> articleIds = products.stream()
                .flatMap(product -> product.getArticles().stream())
                .map(ArticleDto::getArticleId)
                .collect(Collectors.toSet());
        if (checkExistingProducts(products)) {
            // TODO: Add customized error if it has duplicate products
        }
        if (!inventoryService.checkExistingInventory(List.copyOf(articleIds))) {
            // TODO: Add customized error if no existing inventory for product articles
        }
        productRepository.saveAll(products);
    }

    @Override
    public boolean checkExistingProducts(List<Product> products) {
        List<String> productNames = products.stream().map(Product::getName).collect(Collectors.toList());
        return productRepository.existsByNameIn(productNames);
    }
}
