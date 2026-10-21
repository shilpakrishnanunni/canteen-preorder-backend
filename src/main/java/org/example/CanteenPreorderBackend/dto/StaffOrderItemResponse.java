package org.example.CanteenPreorderBackend.dto;

import java.math.BigDecimal;

public record StaffOrderItemResponse (
    String name,
    Integer quantity,
    BigDecimal unitPrice
)   {}
