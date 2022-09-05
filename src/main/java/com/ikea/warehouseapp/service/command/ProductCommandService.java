package com.ikea.warehouseapp.service.command;

import com.ikea.warehouseapp.data.dto.NewProductDto;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.data.mybatis.ProductCreateMapper;
import com.ikea.warehouseapp.data.mybatis.ProductReadMapper;
import com.ikea.warehouseapp.data.mybatis.ProductUpdateMapper;
import com.ikea.warehouseapp.data.repository.ProductRepository;
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

    private ProductReadMapper productReadMapper;

    private ProductCreateMapper productCreateMapper;

    private ProductUpdateMapper productUpdateMapper;

    public List<Product> saveAllProducts(List<Product> products) {
        final List<Product> createdProducts = productRepository.saveAll(products);
        log.info(NEW_PRODUCTS_LOG, createdProducts.stream().map(Product::getId).collect(Collectors.toList()));
        return createdProducts;
    }

    public void purchaseProduct(Long id) {
        productUpdateMapper.updateProductAvailableStock(id);
    }

    public void addNewProduct(NewProductDto newProductDto) {
        long newProductId = productReadMapper.selectNextProductIdSeq();
        productCreateMapper.addNewProduct(newProductId, newProductDto);
    }
}
