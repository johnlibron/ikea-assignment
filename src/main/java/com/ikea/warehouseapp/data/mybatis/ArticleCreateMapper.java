package com.ikea.warehouseapp.data.mybatis;

import com.ikea.warehouseapp.data.dto.NewArticleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleCreateMapper {

    void addNewInventory(@Param("article") NewArticleDto newArticleDto);
}
