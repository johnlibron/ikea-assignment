package com.ikea.assignment.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ArticleDto {

    @JsonProperty("art_id")
    private String articleId;

    @JsonProperty("amount_of")
    private String amountOf;
}
