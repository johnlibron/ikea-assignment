package com.ikea.warehouseapp.data.json.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductJsonObject {

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private BigDecimal price;

    @JsonProperty(value = "contain_articles", required = true)
    private List<ArticleJsonObject> articles;

    @JsonProperty(value = "articles")
    public List<ArticleJsonObject> getArticles() {
        return articles;
    }

    @JsonProperty(value = "contain_articles")
    public void setArticles(List<ArticleJsonObject> articles) {
        this.articles = articles;
    }
}
