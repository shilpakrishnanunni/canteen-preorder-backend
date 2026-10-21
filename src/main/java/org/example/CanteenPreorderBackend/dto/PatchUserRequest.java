package org.example.CanteenPreorderBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

public record PatchUserRequest(
        String name,
        @Email String email,
        @Min(18) Integer age
) {}
