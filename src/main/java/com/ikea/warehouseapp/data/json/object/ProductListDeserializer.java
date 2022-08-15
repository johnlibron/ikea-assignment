package com.ikea.warehouseapp.data.json.object;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.model.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductListDeserializer extends JsonDeserializer<List<Product>> {

    @Override
    public List<Product> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode nodeTree = parser.readValueAsTree();
        List<Product> products = new ArrayList<>();
        for (JsonNode productNode : nodeTree) {
            Set<ArticleDto> articles = new HashSet<>();
            for (JsonNode articleNode : productNode.get("contain_articles")) {
                articles.add(new ArticleDto(
                    articleNode.get("art_id").asText(),
                    articleNode.get("amount_of").asLong()
                ));
            }
            Product product = new Product();
            product.setName(productNode.get("name").asText());
            product.setPrice(BigDecimal.valueOf(productNode.get("price").asDouble()));
            product.setArticles(articles);
            products.add(product);
        }
        return products;
    }
}
