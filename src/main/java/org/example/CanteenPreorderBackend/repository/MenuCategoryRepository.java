package org.example.CanteenPreorderBackend.repository;

import org.example.CanteenPreorderBackend.entities.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
}
