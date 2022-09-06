package com.ikea.warehouseapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class NewProductDto {

    @NotBlank(message = "can't be empty")
    private String name;

    @NotNull(message = "can't be empty")
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NotEmpty(message = "can't be empty")
    private List<@Valid ProductArticleDto> articles;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class ProductArticleDto {

        @NotBlank(message = "can't be empty")
        private String articleId;

        @NotNull(message = "can't be empty")
        @Min(value = 1L)
        private Long amountOf;
    }
}
