package com.ikea.warehouseapp.controller;

import com.ikea.warehouseapp.data.dto.InventoryDto;
import com.ikea.warehouseapp.data.dto.InventoryIncomingDto;
import com.ikea.warehouseapp.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private static final String NEW_INVENTORY_LOG = "New inventory was created: {}";

    private final InventoryService inventoryService;

    @Operation(summary = "Add new inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "New inventory is added",
            content = {@Content(schema = @Schema(implementation = InventoryDto.class))}),
        @ApiResponse(responseCode = "409", description = "Inventory already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<InventoryDto> addInventory(@Valid @RequestBody InventoryIncomingDto inventoryIncomingDto) {
        final InventoryDto addedInventory = inventoryService.addInventory(inventoryIncomingDto);
        if (addedInventory == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        logger.info(NEW_INVENTORY_LOG, addedInventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedInventory);
    }

    @PostMapping("import")
    public ResponseEntity<Void> importInventory(@RequestParam("path") String path) throws IOException {
        // TODO - Add ApiResponses, Operation, path validation
        inventoryService.importInventory(path);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
