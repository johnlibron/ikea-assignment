package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.ProductArticleDto;
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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        File jsonFile = FileUtils.getJsonFile(path);
        List<Product> products = JsonMapperUtils.toObject(jsonFile, Products.class).getProducts();
        List<Product> existingProducts = productQueryService.findByNameIn(getProductNames(products));
        if (!existingProducts.isEmpty()) {
            throw new ResourceExistsException("Import products " + getProductNames(existingProducts) + " already exists");
        }
        List<String> articleIds = new ArrayList<>(getProductArticleIds(products));
        List<Article> existingArticles = articleQueryService.findByArticleIdIn(articleIds);
        Collection<String> notExistArticleIds = CollectionUtils.removeAll(articleIds, getArticleIds(existingArticles));
        if (!notExistArticleIds.isEmpty()) {
            throw new ResourceNotFoundException("Import product article ids " + notExistArticleIds + " not exists");
        }
        final List<Product> importedProducts = productCommandService.saveAllProducts(products);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.INSTANCE.toDtoList(importedProducts));
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
        List<String> articleIds = new ArrayList<>(getArticleIds(articles));
        List<Article> existingArticles = articleQueryService.findByArticleIdIn(articleIds);
        if (!existingArticles.isEmpty()) {
            throw new ResourceExistsException("Import articles ids " + getArticleIds(existingArticles) + " already exists");
        }
        final List<Article> importedArticles = articleCommandService.saveAllArticles(articles);
        return ResponseEntity.status(HttpStatus.CREATED).body(ArticleMapper.INSTANCE.toDtoList(importedArticles));
    }

    private List<String> getProductNames(List<Product> products) {
        return products.stream().map(Product::getName).collect(Collectors.toList());
    }

    private Set<String> getProductArticleIds(List<Product> products) {
        return products.stream().flatMap(product -> product.getArticles().stream())
                .map(ProductArticleDto::getArticleId).collect(Collectors.toSet());
    }

    private List<String> getArticleIds(List<Article> articles) {
        return articles.stream().map(Article::getArticleId).collect(Collectors.toList());
    }
}
