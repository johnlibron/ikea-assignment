package com.ikea.warehouseapp.service.query;

import com.ikea.warehouseapp.data.Page;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.dto.ProductPageDto;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.data.mybatis.ProductReadService;
import com.ikea.warehouseapp.data.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductQueryService {

    private ProductRepository productRepository;

    private ProductReadService productReadService;

    public List<Product> findByNameIn(List<String> productNames) {
        return productRepository.findByNameIn(productNames);
    }

    public ProductPageDto<AvailableProductDto> findAvailableProducts(Page page) {
        // TODO: Minimize db transactions, check db caching, use hashmap
        long count = productReadService.countAvailableProducts();
        if (count <= 0) {
            return new ProductPageDto<>(new ArrayList<>(), count);
        }
        List<AvailableProductDto> availableProducts = productReadService.findAvailableProducts(page);
        return new ProductPageDto<>(availableProducts, count);
    }

    /*public Long getAvailableInventory(List<ProductArticleDto> articles) {
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
    }*/
}
