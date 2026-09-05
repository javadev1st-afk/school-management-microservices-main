package com.school.userservice.controller;

import com.school.userservice.dto.LoginRequestDTO;
import com.school.userservice.dto.LoginResponseDTO;
import com.school.userservice.dto.UserRegistrationDTO;
import com.school.userservice.service.AuthService;
import com.school.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        log.info("Register request received for username: {}", registrationDTO.getUsername());
        LoginResponseDTO response = authService.register(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Login request received for email: {}", loginRequest.getEmail());
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }
}
