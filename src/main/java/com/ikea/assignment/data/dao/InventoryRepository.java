package com.ikea.assignment.data.dao;

import com.ikea.assignment.data.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
}
