package com.ikea.warehouseapp.data.json.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ArticleJsonObject {

    @JsonProperty(value = "art_id", required = true)
    private String articleId;

    @JsonProperty(value = "amount_of", required = true)
    private String amountOf;
}
