package com.ikea.warehouseapp.service.query;

import com.ikea.warehouseapp.data.Page;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.dto.ProductPageDto;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.data.mybatis.ProductReadMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductQueryService {

    private ProductReadMapper productReadMapper;

    public List<String> findExistingProducts(List<Product> products) {
        return productReadMapper.findExistingProducts(products);
    }

    public ProductPageDto<AvailableProductDto> findAvailableProducts(Page page) {
        // TODO: Minimize db transactions, check db caching, use hashmap
        // TODO: Add page, size, totalPages, sort for ProductPageDto, try use Cursor
        // TODO: Check if Mybatis mapper can support SelectStatementProvider with java code
        // https://stackoverflow.com/questions/17511313/how-to-do-pagination-with-mybatis
        // https://www.javaguides.net/2021/10/spring-boot-pagination-and-sorting-rest-api.html
        // Pageable pageWithTenElements = PageRequest.of(pageNumber-1,10);
        long count = productReadMapper.countAvailableProducts();
        if (count <= 0) {
            return new ProductPageDto<>(new ArrayList<>(), count);
        }
        List<AvailableProductDto> availableProducts = productReadMapper.findAvailableProducts(page);
        return new ProductPageDto<>(availableProducts, count);
    }

    public Optional<AvailableProductDto> findProductAvailableStock(Long id) {
        AvailableProductDto availableProduct = productReadMapper.findProductAvailableStock(id);
        if (availableProduct == null) {
            return Optional.empty();
        }
        return Optional.of(availableProduct);
    }
}
