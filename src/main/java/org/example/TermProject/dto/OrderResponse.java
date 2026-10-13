package org.example.TermProject.dto;

import org.example.TermProject.entities.OrderStatus;

public record OrderResponse(
        String orderToken,
        OrderStatus status
) {}
