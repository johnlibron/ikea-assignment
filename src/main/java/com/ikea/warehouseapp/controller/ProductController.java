package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.Page;
import com.ikea.warehouseapp.data.dto.AvailableProductDto;
import com.ikea.warehouseapp.data.dto.ProductArticleDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.dto.ProductIncomingDto;
import com.ikea.warehouseapp.data.dto.ProductPageDto;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.exception.InsufficientStockException;
import com.ikea.warehouseapp.exception.ResourceNotFoundException;
import com.ikea.warehouseapp.service.ProductService;
import com.ikea.warehouseapp.service.command.ProductCommandService;
import com.ikea.warehouseapp.service.query.ProductQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private static final String NEW_PRODUCT_LOG = "New product was created: {}";

    private final ProductService productService;

    private ProductQueryService productQueryService;

    private ProductCommandService productCommandService;

    @Operation(summary = "Get all products and quantity of each that is an available with the current inventory")
    @ApiResponse(responseCode = "200", description = "Available products were returned", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = AvailableProductDto.class)))
    })
    @GetMapping("available")
    public ResponseEntity<ProductPageDto<AvailableProductDto>> getAvailableProducts(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
            @RequestParam(value = "direction", defaultValue = "desc") String direction) {
        // TODO - Add fetch big data support
        return ResponseEntity.ok(productQueryService.findAvailableProducts(new Page(offset, limit)));
    }

    @Operation(summary = "Purchase a product and update the inventory accordingly")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product was purchased",
            content = {@Content(schema = @Schema(implementation = AvailableProductDto.class))}),
        @ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Insufficient stock", content = @Content)
    })
    @PutMapping("{id}")
    public ResponseEntity<AvailableProductDto> purchaseProduct(@PathVariable("id") Long id) {
        AvailableProductDto availableProduct = productQueryService.findProductAvailableStock(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not exists"));
        if (availableProduct.getQuantity() == 0) {
            throw new InsufficientStockException("Insufficient stock for " + availableProduct.getName());
        }
        productCommandService.purchaseProduct(id);
        availableProduct.setQuantity(availableProduct.getQuantity()-1);
        return ResponseEntity.ok(availableProduct);
    }

    @Operation(summary = "Add new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "New product is added",
            content = {@Content(schema = @Schema(implementation = ProductDto.class))}),
        @ApiResponse(responseCode = "404", description = "Inventory not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Product already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductIncomingDto productIncomingDto) {
        final Optional<Product> optionalProduct = productService.getProductByName(productIncomingDto.getName());
        if (optionalProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        final List<ProductArticleDto> articles = productService.getProductArticles(productIncomingDto);
        logger.info("articles: " + articles);
        if (articles == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final ProductDto addedProduct = productService.addProduct(productIncomingDto);
        if (addedProduct == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        logger.info(NEW_PRODUCT_LOG, addedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
