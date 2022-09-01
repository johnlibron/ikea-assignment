package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.json.Articles;
import com.ikea.warehouseapp.data.json.Products;
import com.ikea.warehouseapp.data.mapper.ArticleMapper;
import com.ikea.warehouseapp.data.mapper.ProductMapper;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.exception.ResourceExistsException;
import com.ikea.warehouseapp.exception.ResourceNotFoundException;
import com.ikea.warehouseapp.service.command.ArticleCommandService;
import com.ikea.warehouseapp.service.command.ProductCommandService;
import com.ikea.warehouseapp.service.query.ArticleQueryService;
import com.ikea.warehouseapp.service.query.ProductQueryService;
import com.ikea.warehouseapp.util.FileUtils;
import com.ikea.warehouseapp.util.JsonMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/imports")
@AllArgsConstructor
public class ImportController {

    private ProductQueryService productQueryService;

    private ArticleQueryService articleQueryService;

    private ProductCommandService productCommandService;

    private ArticleCommandService articleCommandService;

    @Operation(summary = "Import products with articles")
    @ApiResponse(responseCode = "201", description = "Products with articles are imported", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
    })
    @PostMapping("products")
    public ResponseEntity<List<ProductDto>> importProducts(@RequestParam("path") String path) throws IOException {
        // TODO: Add batch insert support, check deadlock scenario, and add logs, path validation
        List<Product> products = JsonMapperUtils.toObject(FileUtils.getJsonFile(path), Products.class).getProducts();
        List<String> existingProducts = productQueryService.findExistingProducts(products);
        if (!existingProducts.isEmpty()) {
            throw new ResourceExistsException("Import products " + existingProducts + " already exists");
        }
        List<String> notExistingProductArticles = articleQueryService.findNotExistingProductArticles(products);
        if (!notExistingProductArticles.isEmpty()) {
            throw new ResourceNotFoundException("Import product article ids " + notExistingProductArticles + " not exists");
        }
        final List<Product> importedProducts = productCommandService.saveAllProducts(products);
        return new ResponseEntity<>(ProductMapper.INSTANCE.toDtoList(importedProducts), HttpStatus.CREATED);
    }

    @Operation(summary = "Import articles")
    @ApiResponse(responseCode = "201", description = "Articles are imported", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class)))
    })
    @PostMapping("articles")
    public ResponseEntity<List<ArticleDto>> importArticles(@RequestParam("path") String path) throws IOException {
        // TODO: Add batch insert support, check deadlock scenario, and add logs, path validation
        // TODO: If update stocks if import article (id and name) has existing data
        List<Article> articles = JsonMapperUtils.toObject(FileUtils.getJsonFile(path), Articles.class).getArticles();
        List<String> existingArticles = articleQueryService.findExistingArticles(articles);
        if (!existingArticles.isEmpty()) {
            throw new ResourceExistsException("Import articles ids " + existingArticles + " already exists");
        }
        final List<Article> importedArticles = articleCommandService.saveAllArticles(articles);
        return new ResponseEntity<>(ArticleMapper.INSTANCE.toDtoList(importedArticles), HttpStatus.CREATED);
    }
}
