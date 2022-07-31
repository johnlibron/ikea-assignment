package com.ikea.assignment.data.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GenericGenerator(name = "InventoryIdGenerator", strategy = "com.ikea.assignment.data.model.id.generator.InventoryIdGenerator")
    @GeneratedValue(generator = "InventoryIdGenerator")
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "stock", nullable = false)
    private Long stock;

    public Inventory() {}

    public Inventory(Long id, String name, Long stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }
}
