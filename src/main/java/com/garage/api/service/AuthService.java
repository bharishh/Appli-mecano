package com.garage.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage.api.dto.AuthResponse;
import com.garage.api.dto.LoginRequest;
import com.garage.api.dto.RegisterRequest;
import com.garage.api.entity.Client;
import com.garage.api.entity.Mecanicien;
import com.garage.api.exception.AuthException;
import com.garage.api.repository.ClientRepository;
import com.garage.api.repository.MecanicienRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository;
    private final MecanicienRepository mecanicienRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;

    // Inscription client
    public AuthResponse register(RegisterRequest request) {
        if (clientRepository.findByEmail(request.getEmail()).isPresent())
            throw new AuthException("Email déjà utilisé");

        Client client = Client.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        clientRepository.save(client);
        String token = jwtService.generateToken(client.getEmail(), "CLIENT");

        return AuthResponse.builder()
                .id(client.getId())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .telephone(client.getTelephone())
                .role("CLIENT")
                .token(token)
                .build();
    }

    // Connexion client ou mécanicien
    public AuthResponse login(LoginRequest request) {

        // Cherche d'abord dans Mecanicien
        var meca = mecanicienRepository.findByEmail(request.getEmail());
        if (meca.isPresent()) {
            Mecanicien m = meca.get();
            if (!passwordEncoder.matches(request.getPassword(), m.getPassword()))
                throw new AuthException("Mot de passe incorrect");

            String token = jwtService.generateToken(m.getEmail(), "MECANICIEN");
            return AuthResponse.builder()
                    .id(m.getId())
                    .nom(m.getNom())
                    .prenom(m.getPrenom())
                    .email(m.getEmail())
                    .telephone(m.getTelephone())
                    .role("MECANICIEN")
                    .token(token)
                    .build();
        }

        // Sinon cherche dans Client
        Client client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Email introuvable"));

        if (!passwordEncoder.matches(request.getPassword(), client.getPassword()))
            throw new AuthException("Mot de passe incorrect");

        String token = jwtService.generateToken(client.getEmail(), "CLIENT");
        return AuthResponse.builder()
                .id(client.getId())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .telephone(client.getTelephone())
                .role("CLIENT")
                .token(token)
                .build();
    }

    // Inscription mécanicien
    public AuthResponse registerMecanicien(RegisterRequest request) {
        Mecanicien meca = Mecanicien.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        mecanicienRepository.save(meca);
        String token = jwtService.generateToken(meca.getEmail(), "MECANICIEN");
        return AuthResponse.builder()
                .id(meca.getId())
                .nom(meca.getNom())
                .prenom(meca.getPrenom())
                .email(meca.getEmail())
                .telephone(meca.getTelephone())
                .role("MECANICIEN")
                .token(token)
                .build();
    }
}