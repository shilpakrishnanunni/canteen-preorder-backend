package org.example.TermProject.dto;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        Boolean available
) {}