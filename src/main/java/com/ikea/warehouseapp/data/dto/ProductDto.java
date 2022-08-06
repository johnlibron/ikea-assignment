package com.ikea.warehouseapp.data.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class ProductDto {

    private String name;

    private BigDecimal price;

    private List<ArticleDto> articles;
}
