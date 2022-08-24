package com.ikea.warehouseapp.service.impl;

import com.ikea.warehouseapp.data.dao.InventoryRepository;
import com.ikea.warehouseapp.data.dto.InventoryDto;
import com.ikea.warehouseapp.data.dto.InventoryIncomingDto;
import com.ikea.warehouseapp.data.model.Inventory;
import com.ikea.warehouseapp.service.InventoryService;
import com.ikea.warehouseapp.service.JsonParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final String INVENTORY_UPDATED_LOG = "Inventory: {} was updated";

    private final InventoryRepository inventoryRepository;

    private final JsonParserService jsonParserService;

    @Transactional
    @Override
    public InventoryDto addInventory(InventoryIncomingDto inventoryIncomingDto) {
        Inventory inventory = new Inventory();
        try {
            BeanUtils.copyProperties(inventoryIncomingDto, inventory);
            Optional<Inventory> optionalInventory = inventoryRepository.findByName(inventory.getName());
            if (optionalInventory.isPresent()) {
                return null;
            }
            inventory = inventoryRepository.save(inventory);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        InventoryDto inventoryDto = new InventoryDto();
        BeanUtils.copyProperties(inventory, inventoryDto);
        return inventoryDto;
    }

    @Transactional
    @Override
    public void importInventory(String pathname) throws IOException {
        // TODO - Add batch insert support, check deadlock scenario, and add logs
        // TODO - Check duplicate inventories (article id, name)
		inventoryRepository.saveAll(jsonParserService.getInventory(pathname));
    }

    @Override
    public boolean checkExistingInventory(List<String> articleIds) {
        return inventoryRepository.existsByArticleIdIn(articleIds);
    }
}
