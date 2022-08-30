package com.ikea.warehouseapp.data.json.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticlesDeserializer extends JsonDeserializer<List<Article>> {

    @Override
    public List<Article> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        JsonNode nodeTree = parser.readValueAsTree();
        List<Article> articles = new ArrayList<>();
        for (JsonNode articleNode : nodeTree) {
            articles.add(new Article(
                    articleNode.get("name").asText(),
                    articleNode.get("stock").asLong(),
                    articleNode.get("art_id").asText()));
        }
        List<String> articleIds = articles.stream()
                .map(Article::getArticleId)
                .collect(Collectors.toList());
        Set<String> duplicateArticles = CollectionUtils.getDuplicateItems(articleIds);
        if (!duplicateArticles.isEmpty()) {
            // TODO: Add customized error
        }
        return articles;
    }
}
