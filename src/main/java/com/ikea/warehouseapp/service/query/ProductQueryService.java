package com.ikea.warehouseapp.service.query;

import com.ikea.warehouseapp.data.dao.ProductRepository;
import com.ikea.warehouseapp.data.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductQueryService {

    private ProductRepository productRepository;

    public List<Product> findByNameIn(List<String> productNames) {
        return productRepository.findByNameIn(productNames);
    }
}
