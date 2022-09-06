package com.ikea.warehouseapp.data.mapper;

import com.ikea.warehouseapp.data.dto.ArticleDto;
import com.ikea.warehouseapp.data.dto.NewArticleDto;
import com.ikea.warehouseapp.data.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    List<ArticleDto> toDtoList(List<Article> articles);

    ArticleDto toDto(NewArticleDto newArticleDto);
}
