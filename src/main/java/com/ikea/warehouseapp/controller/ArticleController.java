package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.dao.ArticleRepository;
import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.ArticleIncomingDto;
import com.ikea.warehouseapp.data.json.Articles;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.service.InventoryService;
import com.ikea.warehouseapp.util.FileUtils;
import com.ikea.warehouseapp.util.JsonMapperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@AllArgsConstructor
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private static final String NEW_INVENTORY_LOG = "New inventory was created: {}";

    private final ArticleRepository articleRepository;

    private final InventoryService inventoryService;

    @Operation(summary = "Add new inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "New inventory is added",
            content = {@Content(schema = @Schema(implementation = ArticleDto.class))}),
        @ApiResponse(responseCode = "409", description = "Inventory already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ArticleDto> addInventory(@Valid @RequestBody ArticleIncomingDto articleIncomingDto) {
        final ArticleDto addedInventory = inventoryService.addInventory(articleIncomingDto);
        if (addedInventory == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        logger.info(NEW_INVENTORY_LOG, addedInventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedInventory);
    }

    @PostMapping("import")
    public ResponseEntity<Void> importInventory(@RequestParam("path") String path) throws IOException {
        // TODO - Add ApiResponses, Operation, path validation
        File jsonFile = FileUtils.getJsonFile(path);
        List<Article> articles = JsonMapperUtils.toObject(jsonFile, Articles.class).getArticle();
        System.out.println("inventory = " + articles);
//        articleRepository.saveAll(inventory);
//        inventoryService.importInventory(path);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}