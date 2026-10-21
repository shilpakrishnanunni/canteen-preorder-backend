package org.example.CanteenPreorderBackend.dto;

public record MenuCategoryResponse(
        Long id,
        String name,
        String slug
) {}
