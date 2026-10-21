package org.example.CanteenPreorderBackend.dto;

public record AuthResponse(
        Long id,
        String name,
        String email,
        String role
) {}
