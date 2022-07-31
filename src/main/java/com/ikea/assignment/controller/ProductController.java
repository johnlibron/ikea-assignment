package com.ikea.assignment.controller;

import com.ikea.assignment.data.dto.AvailableProductDto;
import com.ikea.assignment.data.model.Product;
import com.ikea.assignment.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/v1/products"}, produces = APPLICATION_JSON_VALUE)
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products and quantity of each that is an available with the current inventory")
    @ApiResponse(responseCode = "200", description = "Available products were returned", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = AvailableProductDto.class)))
    })
    @GetMapping("available")
    public ResponseEntity<List<AvailableProductDto>> getAvailableProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAvailableProducts());
    }

    @Operation(summary = "Purchase a product and update the inventory accordingly")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product was purchased and Inventory was updated", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AvailableProductDto.class))
        }),
        @ApiResponse(responseCode = "404", description = "Product/Inventory not found", content = @Content),
        @ApiResponse(responseCode = "409", description = "Insufficient inventory", content = @Content)
    })
    @PutMapping(value = "{productName}")
    public ResponseEntity<AvailableProductDto> purchaseProduct(@PathVariable("productName") String name) {
        final Optional<Product> optionalProduct = productService.getProductByName(name);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final Long availableInventory = productService.getAvailableInventory(optionalProduct.get().getArticles());
        if (availableInventory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (availableInventory == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        productService.purchaseProduct(optionalProduct.get());
        AvailableProductDto availableProductDto = new AvailableProductDto(name, availableInventory - 1);
        return ResponseEntity.status(HttpStatus.OK).body(availableProductDto);
    }
}
