package org.example.TermProject.repository;

import org.example.TermProject.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderToken(String orderToken);
    List<Order> findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(OffsetDateTime startDate, OffsetDateTime endDate);
    List<Order> findByCreatedAtGreaterThanEqual(OffsetDateTime start);
    List<Order> findByCreatedAtLessThan(OffsetDateTime end);
}
