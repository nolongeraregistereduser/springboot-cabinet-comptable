package com.cabinet.springbootcabinetcomptablemanagement.controllers;

import com.cabinet.springbootcabinetcomptablemanagement.dtos.request.LoginRequestDTO;
import com.cabinet.springbootcabinetcomptablemanagement.dtos.response.AuthResponseDTO;
import com.cabinet.springbootcabinetcomptablemanagement.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles authentication-related endpoints
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}

