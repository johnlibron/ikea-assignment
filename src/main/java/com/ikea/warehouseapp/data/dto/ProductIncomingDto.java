package com.ikea.warehouseapp.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductIncomingDto {

    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private String name;

    @JsonProperty(required = true)
    @NotNull
    @DecimalMin(value = "0.01", message = "Price must not be empty")
    private BigDecimal price;

    @JsonProperty(required = true)
    @NotEmpty
    private List<@NotNull ArticleDto> articles;
}
