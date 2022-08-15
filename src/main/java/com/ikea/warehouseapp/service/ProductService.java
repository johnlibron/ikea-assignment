package com.ikea.warehouseapp.service;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.dto.ProductIncomingDto;
import com.ikea.warehouseapp.data.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {

    List<AvailableProductDto> getAvailableProducts();

    Long getAvailableInventory(Set<ArticleDto> articles);

    Optional<Product> getProductByName(String name);

    void purchaseProduct(Product product);

    List<ArticleDto> getProductArticles(ProductIncomingDto productIncomingDto);

    ProductDto addProduct(ProductIncomingDto productIncomingDto);

    void importProducts(String pathname) throws IOException;
}
