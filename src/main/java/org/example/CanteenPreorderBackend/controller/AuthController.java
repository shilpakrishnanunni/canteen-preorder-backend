package org.example.CanteenPreorderBackend.controller;

import jakarta.validation.Valid;
import org.example.CanteenPreorderBackend.dto.AuthResponse;
import org.example.CanteenPreorderBackend.dto.LoginRequest;
import org.example.CanteenPreorderBackend.dto.LoginResponse;
import org.example.CanteenPreorderBackend.dto.RegisterRequest;
import org.example.CanteenPreorderBackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ){
        AuthResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

}
