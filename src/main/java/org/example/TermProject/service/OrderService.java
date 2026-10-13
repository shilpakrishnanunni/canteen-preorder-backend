package org.example.TermProject.service;

import org.example.TermProject.dto.CreateOrderRequest;
import org.example.TermProject.dto.OrderItemRequest;
import org.example.TermProject.dto.OrderResponse;
import org.example.TermProject.dto.OrderTrackingResponse;
import org.example.TermProject.entities.MenuItem;
import org.example.TermProject.entities.Order;
import org.example.TermProject.entities.OrderItem;
import org.example.TermProject.entities.OrderStatus;
import org.example.TermProject.repository.MenuItemRepository;
import org.example.TermProject.repository.OrderItemRepository;
import org.example.TermProject.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    OrderService(
            MenuItemRepository menuItemRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    private record OrderItemData(
            MenuItem menuItem,
            Integer quantity,
            BigDecimal unitPrice
    ) {}

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemData> validatedOrderItems = new ArrayList<>();
        Set<Long> menuItemIds = new HashSet<>();

        for (OrderItemRequest itemRequest: request.items()) {
            if (!menuItemIds.add(itemRequest.menuItemId())){
                throw new RuntimeException("Duplicate menu item: " + itemRequest.menuItemId());
            }

            MenuItem menuItem = menuItemRepository
                    .findById(itemRequest.menuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: "+itemRequest.menuItemId()));

            if(!menuItem.getAvailable()){
                throw new RuntimeException(menuItem.getName() + " is currently unavailable");
            }

            BigDecimal unitPrice = menuItem.getPrice();
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalAmount = totalAmount.add(itemTotal);

            validatedOrderItems.add(
                    new OrderItemData(
                        menuItem,
                        itemRequest.quantity(),
                        unitPrice
                    )
            );

        }
        Order order = new Order();
        order.setOrderToken(generateOrderToken());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        List<OrderItem> orderItems = validatedOrderItems.stream()
                .map(item -> new OrderItem(
                        order,
                        item.menuItem(),
                        item.quantity(),
                        item.unitPrice()
                ))
                .toList();
        orderItemRepository.saveAll(orderItems);

        return new OrderResponse(
                order.getOrderToken(),
                order.getStatus()
        );
    }

    private String generateOrderToken() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0,12)
                .toUpperCase();
    }

    public OrderTrackingResponse trackOrder(String orderToken) {
        Order order = orderRepository
                .findByOrderToken(orderToken)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return new OrderTrackingResponse(
                order.getOrderToken(),
                order.getStatus()
        );
    }

}
