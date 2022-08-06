package com.ikea.warehouseapp.data.dto;

import lombok.Data;

@Data
public class AvailableProductDto {

    private String name;

    private Long quantity;

    public AvailableProductDto(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
