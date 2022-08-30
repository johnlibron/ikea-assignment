package com.ikea.warehouseapp.data.json;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ikea.warehouseapp.data.json.serializer.ArticlesDeserializer;
import com.ikea.warehouseapp.data.model.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Articles {

    @JsonProperty(value = "inventory", required = true)
    @JsonDeserialize(using = ArticlesDeserializer.class)
    private List<Article> articles;
}
