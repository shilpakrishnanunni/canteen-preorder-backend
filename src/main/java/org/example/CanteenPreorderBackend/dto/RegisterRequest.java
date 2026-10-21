package org.example.CanteenPreorderBackend.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull @Min(18) Integer age,
        @NotBlank @Size(min = 8) String password

) {}
