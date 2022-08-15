package com.ikea.warehouseapp.service;

import com.ikea.warehouseapp.data.model.Inventory;
import com.ikea.warehouseapp.data.model.Product;

import java.io.IOException;
import java.util.List;

public interface JsonParserService {

    List<Inventory> getInventory(String pathname) throws IOException;

    List<Product> getProducts(String pathname) throws IOException;
}
