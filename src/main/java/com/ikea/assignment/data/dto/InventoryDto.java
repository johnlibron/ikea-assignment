package com.ikea.assignment.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InventoryDto {

    @JsonProperty("art_id")
    private String articleId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("stock")
    private String stock;
}
