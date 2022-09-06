package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.NewArticleDto;
import com.ikea.warehouseapp.data.mapper.ArticleMapper;
import com.ikea.warehouseapp.data.repository.ArticleRepository;
import com.ikea.warehouseapp.exception.ResourceExistsException;
import com.ikea.warehouseapp.service.command.ArticleCommandService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/articles")
@AllArgsConstructor
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private static final String NEW_INVENTORY_LOG = "New inventory was created: {}";

    private final ArticleRepository articleRepository;

    private ArticleCommandService articleCommandService;

    @Operation(summary = "Add new inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "New inventory is added",
            content = {@Content(schema = @Schema(implementation = ArticleDto.class))}),
        @ApiResponse(responseCode = "409", description = "Inventory already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ArticleDto> addInventory(@Valid @RequestBody NewArticleDto newArticleDto) {
        articleRepository.findByArticleId(newArticleDto.getArticleId()).ifPresent(article -> {
            throw new ResourceExistsException("Article " + article.getArticleId() + " already exists");
        });
        articleRepository.findByName(newArticleDto.getName()).ifPresent(article -> {
            throw new ResourceExistsException("Article name " + article.getName() + " already exists");
        });
        articleCommandService.addNewInventory(newArticleDto);
        return new ResponseEntity<>(ArticleMapper.INSTANCE.toDto(newArticleDto), HttpStatus.CREATED);
    }
}
