package com.ikea.warehouseapp.data.mybatis;

import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleReadMapper {

    List<String> findExistingArticles(@Param("articles") List<Article> articles);

    List<String> findNotExistingProductArticles(@Param("products") List<Product> products);
}
