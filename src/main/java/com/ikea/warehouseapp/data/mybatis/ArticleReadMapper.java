package com.ikea.warehouseapp.data.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleReadMapper {

    List<String> findArticlesByIdIn(@Param("articleIds") List<String> articleIds);

    List<String> findArticlesByIdNotIn(@Param("articleIds") List<String> articleIds);
}
