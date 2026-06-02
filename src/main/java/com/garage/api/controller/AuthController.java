package com.garage.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage.api.dto.LoginRequest;
import com.garage.api.dto.RegisterRequest;
import com.garage.api.entity.Client;
import com.garage.api.entity.Mecanicien;
import com.garage.api.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/garage/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<Client> register(@RequestBody RegisterRequest request)
	{
		return ResponseEntity.ok(authService.register(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<Client> login(@RequestBody LoginRequest request)
	{
		return ResponseEntity.ok(authService.login(request));
	}
	
	 @PostMapping("/login/mecanicien")
	    public ResponseEntity<Mecanicien> loginMecanicien(@RequestBody LoginRequest request) {
	        return ResponseEntity.ok(authService.loginMecanicien(request));
	    }

}