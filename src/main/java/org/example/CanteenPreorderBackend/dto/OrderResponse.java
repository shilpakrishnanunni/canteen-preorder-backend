package org.example.CanteenPreorderBackend.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.example.CanteenPreorderBackend.entities.OrderStatus;

public record OrderResponse(
        Long id,
        String orderToken,
        OrderStatus status,
        BigDecimal totalAmount,
        OffsetDateTime createdAt
) {}
