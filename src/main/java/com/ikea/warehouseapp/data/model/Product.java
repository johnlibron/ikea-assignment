package com.ikea.warehouseapp.data.model;

import com.ikea.warehouseapp.data.dto.ProductArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Product")
@Table(name = "product", schema = "public")
public class Product {

    @Id
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_articles", joinColumns = @JoinColumn(name = "product_id"))
    private List<ProductArticleDto> articles;

    // TODO: Transfer this constructor to Lombok
    public Product(String name, BigDecimal price, List<ProductArticleDto> articles) {
        this.name = name;
        this.price = price;
        this.articles = articles;
    }
}
