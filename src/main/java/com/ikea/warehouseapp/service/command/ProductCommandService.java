package com.ikea.warehouseapp.service.command;

import com.ikea.warehouseapp.data.repository.ProductRepository;
import com.ikea.warehouseapp.data.model.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ProductCommandService {

    private static final String NEW_PRODUCTS_LOG = "New created products: {}";

    private ProductRepository productRepository;

    public List<Product> saveAllProducts(List<Product> products) {
        final List<Product> createdProducts = productRepository.saveAll(products);
        log.info(NEW_PRODUCTS_LOG, createdProducts.stream().map(Product::getId).collect(Collectors.toList()));
        return createdProducts;
    }
}
