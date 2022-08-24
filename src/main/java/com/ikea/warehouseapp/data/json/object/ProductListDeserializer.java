package com.ikea.warehouseapp.data.json.object;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductListDeserializer extends JsonDeserializer<List<Product>> {

    @Override
    public List<Product> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode nodeTree = parser.readValueAsTree();
        List<Product> products = new ArrayList<>();
        for (JsonNode productNode : nodeTree) {
            List<ArticleDto> articles = new ArrayList<>();
            for (JsonNode articleNode : productNode.get("contain_articles")) {
                articles.add(new ArticleDto(
                        articleNode.get("art_id").asText(),
                        articleNode.get("amount_of").asLong()));
            }
            List<String> articleIds = articles.stream()
                    .map(ArticleDto::getArticleId)
                    .collect(Collectors.toList());
            Set<String> duplicateArticles = CollectionUtils.getDuplicateItems(articleIds);
            /*
            if (!duplicateArticles.isEmpty()) {
                // TODO: Add customized error
            }
            */
            products.add(new Product(productNode.get("name").asText(),
                    productNode.get("price").decimalValue(),
                    articles));
        }
        List<String> productNames = products.stream()
                .map(Product::getName).collect(Collectors.toList());
        Set<String> duplicateProducts = CollectionUtils.getDuplicateItems(productNames);
        /*
        if (!duplicateProducts.isEmpty()) {
            // TODO: Add customized error
        }
        */
        return products;
    }
}
