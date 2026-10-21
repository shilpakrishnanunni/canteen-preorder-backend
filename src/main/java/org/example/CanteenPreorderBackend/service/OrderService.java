package org.example.CanteenPreorderBackend.service;

import org.example.CanteenPreorderBackend.dto.*;
import org.example.CanteenPreorderBackend.entities.MenuItem;
import org.example.CanteenPreorderBackend.entities.Order;
import org.example.CanteenPreorderBackend.entities.OrderItem;
import org.example.CanteenPreorderBackend.entities.OrderStatus;
import org.example.CanteenPreorderBackend.repository.MenuItemRepository;
import org.example.CanteenPreorderBackend.repository.OrderItemRepository;
import org.example.CanteenPreorderBackend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
                order.getId(),
                order.getOrderToken(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }

    private String generateOrderToken() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0,12)
                .toUpperCase();
    }

    @Transactional(readOnly = true)
    public OrderTrackingResponse trackOrder(String orderToken) {
        Order order = orderRepository
                .findByOrderToken(orderToken)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItemTrackingResponse> items = orderItemRepository.findByOrderId(order.getId())
                .stream()
                .map(item -> new OrderItemTrackingResponse(
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();
        return new OrderTrackingResponse(
                order.getOrderToken(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getTotalAmount(),
                items
        );
    }

    @Transactional(readOnly = true)
    public List<StaffOrderResponse> getOrdersByDate(LocalDate startDate, LocalDate endDate){
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must be on or after startDate");
        }

        OffsetDateTime start = startDate == null ? null : startDate.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = endDate == null ? null : endDate.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        List<Order> orders;

        if (start != null && end != null) {
            orders = orderRepository
                    .findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(start, end);
        } else if (start != null) {
            orders = orderRepository.findByCreatedAtGreaterThanEqual(start);
        } else if (end != null) {
            orders = orderRepository.findByCreatedAtLessThan(end);
        } else {
            orders = orderRepository.findAll();
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

    @Transactional
    public void updateOrderStatus(String orderToken, OrderStatus newStatus) {
        Order order = orderRepository.findByOrderToken(orderToken)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        OrderStatus currentStatus = order.getStatus();

        boolean validTransition =
                (currentStatus == OrderStatus.PLACED &&
                        (newStatus == OrderStatus.ACCEPTED ||
                                newStatus == OrderStatus.REJECTED))
                        ||
                        (currentStatus == OrderStatus.ACCEPTED &&
                                newStatus == OrderStatus.COMPLETED);

        if (!validTransition) {
            throw new IllegalArgumentException(
                    "Cannot change order status from " + currentStatus +
                            " to " + newStatus
            );
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(OffsetDateTime.now());
    }
}
