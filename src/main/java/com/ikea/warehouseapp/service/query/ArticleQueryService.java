package com.ikea.warehouseapp.service.query;

import com.ikea.warehouseapp.data.repository.ArticleRepository;
import com.ikea.warehouseapp.data.model.Article;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleQueryService {

    private ArticleRepository articleRepository;

    public List<Article> findByArticleIdIn(List<String> articleIds) {
        return articleRepository.findByArticleIdIn(articleIds);
    }
}
