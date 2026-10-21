package org.example.TermProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.TermProject.entities.OrderStatus;

public record UpdateOrderStatusRequest(
        @NotBlank String orderToken,
        @NotNull OrderStatus status
) {}
