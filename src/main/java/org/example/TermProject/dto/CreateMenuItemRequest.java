package org.example.TermProject.dto;

import java.math.BigDecimal;

public record CreateMenuItemRequest(
        Long categoryId,
        String name,
        String description,
        BigDecimal price,
        String imageUrl
) {}