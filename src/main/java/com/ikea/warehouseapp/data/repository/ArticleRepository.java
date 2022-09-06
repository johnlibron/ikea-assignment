package com.ikea.warehouseapp.data.repository;

import com.ikea.warehouseapp.data.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByArticleId(String articleId);

    Optional<Article> findByName(String name);
}
