package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dao.ArticleRepository;
import com.ikea.warehouseapp.data.dao.ProductRepository;
import com.ikea.warehouseapp.data.dto.ProductArticleDto;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.dto.ProductIncomingDto;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.service.InventoryService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final ProductRepository productRepository;

    private final InventoryService inventoryService;

    private final ArticleRepository articleRepository;

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
    public Long getAvailableInventory(List<ProductArticleDto> articles) {
        List<Article> articleList = articleRepository.findAll();
        long minQuantity = 0;
        for (ProductArticleDto article : articles) {
            Optional<Long> optionalStock = articleList.stream()
                    .filter(e -> e.getArticleId().equals(article.getArticleId()))
                    .findFirst().map(Article::getStock);
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

    @Override
    public void purchaseProduct(Product product) {
        List<Integer> inventoryIds = new ArrayList<>();
        List<Article> inventories = new ArrayList<>();
        for (ProductArticleDto article : product.getArticles()) {
            Optional<Article> optionalInventory = articleRepository.findByArticleId(article.getArticleId());
            optionalInventory.ifPresent(inventory -> {
                inventory.setStock(inventory.getStock() - article.getAmountOf());
                articleRepository.save(inventory);
            });
        }
//        throw new UserNotFoundException("User not found");
        // employeeRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
        log.info(INVENTORY_UPDATED_LOG, inventoryIds);
    }

    @Override
    public List<ProductArticleDto> getProductArticles(ProductIncomingDto productIncomingDto) {
        List<ProductArticleDto> articles = new ArrayList<>();
        if (productIncomingDto.getArticles().size() > 0) {
            Product product = new Product();
            product.setName(productIncomingDto.getName());
            product.setPrice(productIncomingDto.getPrice());
            for (ProductArticleDto productArticleDto : productIncomingDto.getArticles()) {
                // TODO
//                Optional<Inventory> optionalInventory = inventoryRepository.findByName(articleDto.getName());
                Optional<Article> optionalInventory = Optional.empty();
                if (optionalInventory.isEmpty()) {
                    return null;
                }
                articles.add(new ProductArticleDto(
                    optionalInventory.get().getArticleId(),
                    productArticleDto.getAmountOf()
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
        List<ProductArticleDto> articles = new ArrayList<>();
        for (ProductArticleDto productArticleDto : productIncomingDto.getArticles()) {
            ProductArticleDto article = new ProductArticleDto();
            BeanUtils.copyProperties(productArticleDto, article);
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
        /*List<Product> products = jsonParserService.getProducts(pathname);
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
        productRepository.saveAll(products);*/
    }

    @Override
    public boolean checkExistingProducts(List<Product> products) {
        List<String> productNames = products.stream().map(Product::getName).collect(Collectors.toList());
//        return productRepository.existsByNameIn(productNames);
        return false;
    }
}
