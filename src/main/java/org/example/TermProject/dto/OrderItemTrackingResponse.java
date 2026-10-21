package org.example.TermProject.dto;

import java.math.BigDecimal;

public record OrderItemTrackingResponse(
        String itemName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
