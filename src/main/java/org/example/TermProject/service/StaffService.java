package org.example.TermProject.service;


import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
//import org.example.TermProject.dto.OrderResponse;
import org.example.TermProject.dto.StaffOrderResponse;
import org.example.TermProject.dto.StaffOrderItemResponse;
import org.example.TermProject.entities.Order;
import org.example.TermProject.entities.OrderStatus;
import org.example.TermProject.repository.OrderRepository;
import org.example.TermProject.repository.OrderItemRepository;

import java.util.List;

@Service 
public class StaffService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public StaffService(
        OrderItemRepository orderItemRepository,
        OrderRepository orderRepository
    ){
    this.orderItemRepository = orderItemRepository;
    this.orderRepository = orderRepository;
    }



@Transactional(readOnly = true)
 public List<StaffOrderResponse> getOrdersByStatus(List<OrderStatus> statuses){

        List<Order> orders;

        if (statuses == null || statuses.isEmpty()) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByStatusIn(statuses);
        }

        return orders.stream()
        .map(order-> new StaffOrderResponse(
            order.getId(),
            order.getOrderToken(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getCreatedAt(),
            order.getItems().stream()
                .map(item -> new StaffOrderItemResponse(
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    item.getUnitPrice()
                ))
                .toList()
        ))
        .toList();
    }

    private StaffOrderResponse toStaffOrderResponse(Order order) {
        List<StaffOrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new StaffOrderItemResponse(
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

        return new StaffOrderResponse(
                order.getId(),
                order.getOrderToken(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }
}
