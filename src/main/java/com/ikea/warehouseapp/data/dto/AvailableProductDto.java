package com.ikea.warehouseapp.data.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class AvailableProductDto {

    private String name;

    private double price;

    private Long quantity;

    public AvailableProductDto(String name, double price, Long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
