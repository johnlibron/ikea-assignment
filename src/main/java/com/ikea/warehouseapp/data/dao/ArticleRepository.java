package com.ikea.warehouseapp.data.dao;

import com.ikea.warehouseapp.data.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<Article> findByName(String name);

    Optional<Article> findByArticleId(String id);

    boolean existsByArticleIdIn(List<String> articleIds);

    List<Article> findByArticleIdIn(List<String> articleIds);
}
