package com.ikea.warehouseapp.service.query;

import com.ikea.warehouseapp.data.model.Article;
import com.ikea.warehouseapp.data.model.Product;
import com.ikea.warehouseapp.data.mybatis.ArticleReadMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleQueryService {

    private ArticleReadMapper articleReadMapper;

    public List<String> findExistingArticles(List<Article> articles) {
        return articleReadMapper.findExistingArticles(articles);
    }

    public List<String> findNotExistingProductArticles(List<Product> products) {
        return articleReadMapper.findNotExistingProductArticles(products);
    }
}
