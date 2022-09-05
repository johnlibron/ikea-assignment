package com.ikea.warehouseapp.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvailableProductDto {

    private String name;
    private double price;
    private Long quantity;

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
