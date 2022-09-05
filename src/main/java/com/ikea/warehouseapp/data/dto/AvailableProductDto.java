package com.ikea.warehouseapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableProductDto {

    private String name;
    private double price;
    private Long quantity;
}
