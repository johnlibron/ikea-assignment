package com.ikea.warehouseapp.data.json.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ikea.warehouseapp.data.model.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryListDeserializer extends JsonDeserializer<List<Article>> {

    @Override
    public List<Article> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode nodeTree = parser.readValueAsTree();
        List<Article> inventories = new ArrayList<>();
        for (JsonNode inventoryNode : nodeTree) {
            Article article = new Article();
            article.setName(inventoryNode.get("name").asText());
            article.setStock(inventoryNode.get("stock").asLong());
            article.setArticleId(inventoryNode.get("art_id").asText());
            inventories.add(article);
        }
        return inventories;
    }
}
