package org.example.CanteenPreorderBackend.repository;

import org.example.CanteenPreorderBackend.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}
