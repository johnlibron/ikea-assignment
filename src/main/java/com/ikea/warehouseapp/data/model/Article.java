package com.ikea.warehouseapp.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Article")
@Table(name = "article", schema = "public")
public class Article {

    @Id
    @SequenceGenerator(name = "article_id_seq", sequenceName = "article_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "stock", nullable = false)
    private Long stock;

    @Column(name = "article_id", nullable = false, unique = true)
    private String articleId;

    // TODO: Transfer this constructor to Lombok
    public Article(String name, Long stock, String articleId) {
        this.name = name;
        this.stock = stock;
        this.articleId = articleId;
    }
}
