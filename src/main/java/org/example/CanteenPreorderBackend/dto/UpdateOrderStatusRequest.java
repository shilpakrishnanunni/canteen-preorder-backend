package org.example.CanteenPreorderBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.CanteenPreorderBackend.entities.OrderStatus;

public record UpdateOrderStatusRequest(
        @NotBlank String orderToken,
        @NotNull OrderStatus status
) {}
