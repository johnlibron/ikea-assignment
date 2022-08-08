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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Article")
@Table(name = "article")
public class Article {

    @Id
    @SequenceGenerator(name = "article_sequence", sequenceName = "article_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "amount_of", nullable = false)
    private Long amountOf;

    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    private Product product;
}
