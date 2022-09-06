package com.ikea.warehouseapp.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class NewArticleDto {

    @NotBlank(message = "can't be empty")
    private String articleId;

    @NotBlank(message = "can't be empty")
    private String name;

    @NotNull(message = "can't be empty")
    @Min(value = 1L)
    private Long stock;
}
