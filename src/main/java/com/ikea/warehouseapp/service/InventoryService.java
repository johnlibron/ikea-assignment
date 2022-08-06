package com.ikea.warehouseapp.service;

import com.ikea.warehouseapp.data.dto.InventoryDto;
import com.ikea.warehouseapp.data.dto.InventoryIncomingDto;

public interface InventoryService {

    InventoryDto addInventory(InventoryIncomingDto inventoryIncomingDto);
}
