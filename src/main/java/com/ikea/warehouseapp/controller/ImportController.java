package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.NewProductDto.ProductArticleDto;
import com.ikea.warehouseapp.data.dto.ProductDto;
import com.ikea.warehouseapp.data.json.Articles;
import com.ikea.warehouseapp.data.json.Products;
import com.ikea.warehouseapp.data.mapper.ArticleMapper;
import com.ikea.warehouseapp.data.mapper.ProductMapper;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.data.mybatis.ArticleReadMapper;
import com.ikea.warehouseapp.data.mybatis.ProductReadMapper;
import com.ikea.warehouseapp.exception.ResourceExistsException;
import com.ikea.warehouseapp.exception.ResourceNotFoundException;
import com.ikea.warehouseapp.service.command.ArticleCommandService;
import com.ikea.warehouseapp.service.command.ProductCommandService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/imports")
@AllArgsConstructor
public class ImportController {

    private ProductReadMapper productReadMapper;

    private ArticleReadMapper articleReadMapper;

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
        List<String> productNames = products.stream().map(Product::getName).collect(Collectors.toList());
        List<String> existProducts = productReadMapper.findProductsByNameIn(productNames);
        if (!existProducts.isEmpty()) {
            throw new ResourceExistsException("Import products " + existProducts + " already exists");
        }
        Set<String> articleIds = products.stream()
                .flatMap(product -> product.getArticles().stream())
                .map(ProductArticleDto::getArticleId).collect(Collectors.toSet());
        List<String> notExistArticles = articleReadMapper.findArticlesByIdNotIn(new ArrayList<>(articleIds));
        if (!notExistArticles.isEmpty()) {
            throw new ResourceNotFoundException("Import product article ids " + notExistArticles + " not exists");
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
        List<String> articleIds = articles.stream().map(Article::getArticleId).collect(Collectors.toList());
        List<String> existArticles = articleReadMapper.findArticlesByIdIn(articleIds);
        if (!existArticles.isEmpty()) {
            throw new ResourceExistsException("Import articles ids " + existArticles + " already exists");
        }
        final List<Article> importedArticles = articleCommandService.saveAllArticles(articles);
        return new ResponseEntity<>(ArticleMapper.INSTANCE.toDtoList(importedArticles), HttpStatus.CREATED);
    }
}
