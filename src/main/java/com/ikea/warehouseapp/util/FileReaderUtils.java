package com.ikea.warehouseapp.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.ikea.warehouseapp.data.json.object.InventoryJsonObject;
import com.ikea.warehouseapp.data.json.object.ProductJsonObject;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.List;

public class FileReaderUtils {
    public static List<ProductJsonObject> getProducts(String productsJsonFile) throws IOException {
        final JsonFactory factory = new JsonFactory();
        final JsonParser parser = factory.createParser(ResourceUtils.getFile("classpath:" + productsJsonFile));
        if (parser.nextToken() != JsonToken.START_OBJECT) {
            throw new IOException("expected file to contain JSON object");
        }
        parser.nextToken();
        if (!"products".equals(parser.getCurrentName())) {
            throw new IOException("expected 'products' key in JSON object");
        }
        parser.nextToken();
        // use bean for this
        // ObjectMapper
        final JsonMapper mapper = JsonMapper.builder(factory).build();
        return mapper.readValue(parser, new TypeReference<>(){});
    }

    public static List<InventoryJsonObject> getInventory(String inventoryJsonFile) throws IOException {
        final JsonFactory factory = new JsonFactory();
        final JsonParser parser = factory.createParser(ResourceUtils.getFile("classpath:" + inventoryJsonFile));
        if (parser.nextToken() != JsonToken.START_OBJECT) {
            throw new IOException("expected file to contain JSON object");
        }
        parser.nextToken();
        if (!"inventory".equals(parser.getCurrentName())) {
            throw new IOException("expected 'inventory' key in JSON object");
        }
        parser.nextToken();

        final JsonMapper mapper = JsonMapper.builder(factory).build();
        return mapper.readValue(parser, new TypeReference<>(){});
    }
}
