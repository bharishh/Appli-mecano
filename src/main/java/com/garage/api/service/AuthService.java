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
import com.garage.api.utils.XssUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository;
    private final MecanicienRepository mecanicienRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtService jwtService;
    
    
    


    public AuthResponse register(RegisterRequest request) {
    	  // Validation XSS
        if (!XssUtils.isValidName(request.getNom()))
            throw new AuthException("Nom invalide");
        if (!XssUtils.isValidName(request.getPrenom()))
            throw new AuthException("Prénom invalide");
        if (!XssUtils.isValidEmail(request.getEmail()))
            throw new AuthException("Email invalide");
        if (!XssUtils.isValidPhone(request.getTelephone()))
            throw new AuthException("Téléphone invalide");

    	
        if (clientRepository.findByEmail(request.getEmail()).isPresent())
            throw new AuthException("Email déjà utilisé");


        Client client = Client.builder()
                .nom(XssUtils.sanitize(request.getNom()))
                .prenom(XssUtils.sanitize(request.getPrenom()))
                .email(request.getEmail().toLowerCase().trim())
                .telephone(XssUtils.sanitize(request.getTelephone()))
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
    
   

 
    public AuthResponse login(LoginRequest request) {
    	
    	// Validation email
        if (!XssUtils.isValidEmail(request.getEmail()))
            throw new AuthException("Email invalide");

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

    
    

    public AuthResponse registerMecanicien(RegisterRequest request) {
    	
    	 // Validation XSS
        if (!XssUtils.isValidName(request.getNom()))
            throw new AuthException("Nom invalide");
        if (!XssUtils.isValidName(request.getPrenom()))
            throw new AuthException("Prénom invalide");
        if (!XssUtils.isValidEmail(request.getEmail()))
            throw new AuthException("Email invalide");
        if (!XssUtils.isValidPhone(request.getTelephone()))
            throw new AuthException("Téléphone invalide");
        
        
        Mecanicien meca = Mecanicien.builder()
                .nom(XssUtils.sanitize(request.getNom()))
                .prenom(XssUtils.sanitize(request.getPrenom()))
                .email(request.getEmail().toLowerCase().trim())
                .telephone(XssUtils.sanitize(request.getTelephone()))
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