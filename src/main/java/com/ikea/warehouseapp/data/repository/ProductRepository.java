package com.ikea.warehouseapp.data.repository;

import com.ikea.warehouseapp.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String productName);
}
