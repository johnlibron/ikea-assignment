package com.ikea.warehouseapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductPageDto<T> {

    private List<T> content;
    private long totalElements;
}
