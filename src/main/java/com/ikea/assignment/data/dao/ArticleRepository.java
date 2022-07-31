package com.ikea.assignment.data.dao;

import com.ikea.assignment.data.model.Article;
import com.ikea.assignment.data.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
