package org.example.CanteenPreorderBackend.repository;

import org.example.CanteenPreorderBackend.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByAvailableTrue();
    List<MenuItem> findByCategorySlugAndAvailableTrue(String slug);
}
