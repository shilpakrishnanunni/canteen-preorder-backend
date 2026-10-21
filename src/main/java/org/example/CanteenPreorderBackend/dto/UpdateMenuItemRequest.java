package org.example.CanteenPreorderBackend.dto;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        Long menuItemId,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Boolean available
) {}