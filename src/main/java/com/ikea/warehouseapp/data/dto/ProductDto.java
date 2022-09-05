package com.ikea.warehouseapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String name;

    private BigDecimal price;

    private List<ProductArticleDto> articles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductArticleDto {

        private String articleId;

        private Long amountOf;
    }
}
