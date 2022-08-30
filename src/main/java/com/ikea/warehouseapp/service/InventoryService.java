package com.ikea.warehouseapp.service;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.ArticleIncomingDto;

import java.io.IOException;
import java.util.List;

public interface InventoryService {

    ArticleDto addInventory(ArticleIncomingDto articleIncomingDto);

    void importInventory(String pathname) throws IOException;

    boolean checkExistingInventory(List<String> articleIds);
}
