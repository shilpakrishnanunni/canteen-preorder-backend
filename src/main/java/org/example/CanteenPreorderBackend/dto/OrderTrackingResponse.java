package org.example.CanteenPreorderBackend.dto;

import org.example.CanteenPreorderBackend.entities.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderTrackingResponse(
        String orderToken,
        OrderStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        BigDecimal totalAmount,
        List<OrderItemTrackingResponse> orderItems
) {}
