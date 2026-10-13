package org.example.TermProject.dto;

import org.example.TermProject.entities.OrderStatus;

public record OrderTrackingResponse(
        String orderToken,
        OrderStatus status
) {}
