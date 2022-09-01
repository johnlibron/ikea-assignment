package com.ikea.warehouseapp.service;

import com.ikea.warehouseapp.data.dto.ProductArticleDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.dto.ProductIncomingDto;
import com.ikea.warehouseapp.data.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProductByName(String name);

    List<ProductArticleDto> getProductArticles(ProductIncomingDto productIncomingDto);

    ProductDto addProduct(ProductIncomingDto productIncomingDto);
}
