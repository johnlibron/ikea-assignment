package com.ikea.assignment.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class AvailableProductDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private Long quantity;

    public AvailableProductDto(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
