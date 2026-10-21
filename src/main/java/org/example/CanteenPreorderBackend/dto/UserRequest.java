package org.example.CanteenPreorderBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// immutable data carriers
public record UserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull @Min(18) Integer age
) {}
