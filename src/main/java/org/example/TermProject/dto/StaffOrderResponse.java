package org.example.TermProject.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.math.BigDecimal;

import org.example.TermProject.entities.OrderStatus;

public record StaffOrderResponse (
     Long id,
        String orderToken,
        OrderStatus status,
        BigDecimal totalAmount,
        OffsetDateTime createdAt,
        List<StaffOrderItemResponse> items
){}
