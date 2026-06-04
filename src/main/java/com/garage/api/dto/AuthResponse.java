package com.garage.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;
    private String token;
}