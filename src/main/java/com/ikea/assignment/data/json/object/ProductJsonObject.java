package com.ikea.assignment.data.json.object;

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
}
