package com.ikea.assignment.data.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "art_id", nullable = false)
    private String articleId;

    @Column(name = "amount_of", nullable = false)
    private Long amountOf;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    public Article() {}

    public Article(String articleId, Long amountOf) {
        this.articleId = articleId;
        this.amountOf = amountOf;
    }

    public Article(String articleId, Long amountOf, Product product) {
        this.articleId = articleId;
        this.amountOf = amountOf;
        this.product = product;
    }
}
