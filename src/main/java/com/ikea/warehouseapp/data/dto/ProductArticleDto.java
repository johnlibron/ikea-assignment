package com.ikea.warehouseapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProductArticleDto {

    @NotEmpty
    @NotBlank
    private String articleId;

    @NotNull
    @Min(value = 1L, message = "must not be empty")
    private Long amountOf;
}
