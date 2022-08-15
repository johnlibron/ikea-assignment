package com.ikea.warehouseapp.service;

import com.ikea.warehouseapp.data.dto.InventoryDto;
import com.ikea.warehouseapp.data.dto.InventoryIncomingDto;

import java.io.IOException;

public interface InventoryService {

    InventoryDto addInventory(InventoryIncomingDto inventoryIncomingDto);

    void importInventory(String pathname) throws IOException;
}
