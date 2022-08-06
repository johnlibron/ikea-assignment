package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dao.InventoryRepository;
import com.ikea.warehouseapp.data.dto.InventoryDto;
import com.ikea.warehouseapp.data.dto.InventoryIncomingDto;
import com.ikea.warehouseapp.data.model.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class InventoryServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public InventoryDto addInventory(InventoryIncomingDto inventoryIncomingDto) {
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryIncomingDto, inventory);
        Optional<Inventory> optionalInventory = inventoryRepository.findByName(inventory.getName());
        if (optionalInventory.isEmpty()) {
            return null;
        }
        inventory = inventoryRepository.save(inventory);
        InventoryDto inventoryDto = new InventoryDto();
        BeanUtils.copyProperties(inventory, inventoryDto);
        return inventoryDto;
    }
}