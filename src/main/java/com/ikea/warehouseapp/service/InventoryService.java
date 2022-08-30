package com.ikea.warehouseapp.service;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.ArticleIncomingDto;

public interface InventoryService {

    ArticleDto addInventory(ArticleIncomingDto articleIncomingDto);
}
