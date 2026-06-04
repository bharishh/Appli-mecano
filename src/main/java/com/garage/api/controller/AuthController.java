package com.garage.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage.api.dto.AuthResponse;
import com.garage.api.dto.LoginRequest;
import com.garage.api.dto.RegisterRequest;
import com.garage.api.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/garage/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    // Inscription client
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // Connexion client ou mécanicien (un seul endpoint)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Inscription mécanicien (une seule fois)
    @PostMapping("/register/mecanicien")
    public ResponseEntity<AuthResponse> registerMecanicien(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerMecanicien(request));
    }
}