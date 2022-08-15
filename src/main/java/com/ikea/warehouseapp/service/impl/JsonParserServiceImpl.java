package com.ikea.warehouseapp.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.ikea.warehouseapp.data.json.object.InventoryJson;
import com.ikea.warehouseapp.data.json.object.ProductJson;
import com.ikea.warehouseapp.data.model.Inventory;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.service.JsonParserService;
import com.ikea.warehouseapp.util.JsonMapperUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class JsonParserServiceImpl implements JsonParserService {
    @Override
    public List<Inventory> getInventory(String pathname) throws IOException {
        JsonParser parser = null;
        try {
            final JsonFactory factory = new JsonFactory();
            parser = factory.createParser(new File(pathname));
            return JsonMapperUtils.toObject(InventoryJson.class, parser).getInventory();
        } catch(IOException e) {
            throw new IOException();
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Override
    public List<Product> getProducts(String pathname) throws IOException {
        JsonParser parser = null;
        try {
            final JsonFactory factory = new JsonFactory();
            parser = factory.createParser(new File(pathname));
            return JsonMapperUtils.toObject(ProductJson.class, parser).getProducts();
        } catch(IOException e) {
            throw new IOException();
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }
}
