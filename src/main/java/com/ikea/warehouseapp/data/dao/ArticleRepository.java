package com.ikea.warehouseapp.data.dao;

import com.ikea.warehouseapp.data.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
