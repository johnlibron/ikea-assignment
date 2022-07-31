package com.ikea.assignment.data.json.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InventoryJsonObject {

    @JsonProperty(value = "art_id", required = true)
    private String articleId;

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String stock;


}
