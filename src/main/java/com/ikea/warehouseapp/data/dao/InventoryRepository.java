package com.ikea.warehouseapp.data.dao;

import com.ikea.warehouseapp.data.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByName(String name);

    Optional<Inventory> findByArticleId(String id);

    boolean existsByArticleIdIn(List<String> articleIds);
}
