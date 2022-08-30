package com.ikea.warehouseapp.service.command;

import com.ikea.warehouseapp.data.dao.ArticleRepository;
import com.ikea.warehouseapp.data.model.Article;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ArticleCommandService {

    private static final String NEW_ARTICLES_LOG = "New created articles: {}";

    private ArticleRepository articleRepository;

    public List<Article> saveAllArticles(List<Article> articles) {
        final List<Article> createdArticles = articleRepository.saveAll(articles);
        log.info(NEW_ARTICLES_LOG, createdArticles.stream().map(Article::getId).collect(Collectors.toList()));
        return createdArticles;
    }
}
