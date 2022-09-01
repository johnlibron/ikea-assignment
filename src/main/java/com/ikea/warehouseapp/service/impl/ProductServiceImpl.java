package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dto.ProductArticleDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.dto.ProductIncomingDto;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.data.repository.ArticleRepository;
import com.ikea.warehouseapp.data.repository.ProductRepository;
import com.ikea.warehouseapp.service.InventoryService;
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
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final ProductRepository productRepository;

    private final InventoryService inventoryService;

    private final ArticleRepository articleRepository;

    @Override
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
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
}
