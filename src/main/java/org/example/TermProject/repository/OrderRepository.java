package org.example.TermProject.repository;

import org.example.TermProject.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.TermProject.entities.OrderStatus;
    import java.util.List;

import java.util.Optional;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderToken(String orderToken);
    List<Order> findByStatusIn(List<OrderStatus> statuses);

}
