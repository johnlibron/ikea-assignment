package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dao.InventoryRepository;
import com.ikea.warehouseapp.data.dto.InventoryDto;
import com.ikea.warehouseapp.data.dto.InventoryIncomingDto;
import com.ikea.warehouseapp.data.model.Inventory;
import com.ikea.warehouseapp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final InventoryRepository inventoryRepository;

    @Transactional
    @Override
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
