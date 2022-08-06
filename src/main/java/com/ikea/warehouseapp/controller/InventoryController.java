package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.dto.InventoryDto;
import com.ikea.warehouseapp.data.dto.InventoryIncomingDto;
import com.ikea.warehouseapp.service.impl.InventoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/v1/inventory", produces = APPLICATION_JSON_VALUE)
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private static final String NEW_INVENTORY_LOG = "New inventory was created: {}";

    private final InventoryServiceImpl inventoryServiceImpl;

    @Autowired
    public InventoryController(InventoryServiceImpl inventoryService) {
        this.inventoryServiceImpl = inventoryService;
    }

    @Operation(summary = "Add new inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "New inventory is added", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = InventoryDto.class))
        }),
        @ApiResponse(responseCode = "409", description = "Inventory already exists", content = @Content)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<InventoryDto> addInventory(@Valid @RequestBody InventoryIncomingDto inventoryIncomingDto) {
        final InventoryDto addedInventory = inventoryServiceImpl.addInventory(inventoryIncomingDto);
        if (addedInventory == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        logger.info(NEW_INVENTORY_LOG, addedInventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedInventory);
    }
}
