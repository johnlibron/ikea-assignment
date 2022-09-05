package com.ikea.warehouseapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProductArticleDto {

    @NotEmpty(message = "can't be empty")
    private String articleId;

    @NotNull(message = "can't be empty")
    @Min(value = 1L)
    private Long amountOf;
}
