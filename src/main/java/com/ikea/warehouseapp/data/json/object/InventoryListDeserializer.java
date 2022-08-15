package com.ikea.warehouseapp.data.json.object;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ikea.warehouseapp.data.model.Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryListDeserializer extends JsonDeserializer<List<Inventory>> {

    @Override
    public List<Inventory> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode nodeTree = parser.readValueAsTree();
        List<Inventory> inventories = new ArrayList<>();
        for (JsonNode inventoryNode : nodeTree) {
            Inventory inventory = new Inventory();
            inventory.setName(inventoryNode.get("name").asText());
            inventory.setStock(inventoryNode.get("stock").asLong());
            inventory.setArticleId(inventoryNode.get("art_id").asText());
            inventories.add(inventory);
        }
        return inventories;
    }
}
