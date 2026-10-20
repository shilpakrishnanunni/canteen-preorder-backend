package org.example.TermProject.controller;

import jakarta.validation.Valid;
import org.example.TermProject.dto.CreateOrderRequest;
import org.example.TermProject.dto.OrderResponse;
import org.example.TermProject.dto.OrderTrackingResponse;
import org.example.TermProject.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{orderToken}")
    public OrderTrackingResponse trackOrder(
            @PathVariable String orderToken
    ) {
        // TODO return created_at, order items, total cost?
        return orderService.trackOrder(orderToken);
    }

    // TODO update/cancel order

}
