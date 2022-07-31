package com.ikea.assignment.data.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @Column(name = "art_id", nullable = false)
    private String articleId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "stock", nullable = false)
    private Long stock;

    public Inventory() {}

    public Inventory(String articleId, String name, Long stock) {
        this.articleId = articleId;
        this.name = name;
        this.stock = stock;
    }
}
