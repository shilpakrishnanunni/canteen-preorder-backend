package org.example.CanteenPreorderBackend.dto;

import java.math.BigDecimal;

public record OrderItemTrackingResponse(
        String itemName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
