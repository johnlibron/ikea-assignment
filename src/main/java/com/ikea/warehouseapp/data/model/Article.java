package com.ikea.warehouseapp.data.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount_of", nullable = false)
    private Long amountOf;

    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    public Article() {}

    public Article(Long amountOf, Long inventoryId) {
        this.amountOf = amountOf;
        this.inventoryId = inventoryId;
    }

    public Article(Long amountOf, Long inventoryId, Product product) {
        this.amountOf = amountOf;
        this.inventoryId = inventoryId;
        this.product = product;
    }
}
