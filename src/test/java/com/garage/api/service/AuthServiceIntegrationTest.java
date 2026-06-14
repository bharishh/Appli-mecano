package com.garage.api.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.garage.api.dto.LoginRequest;
import com.garage.api.dto.RegisterRequest;
import com.garage.api.exception.AuthException;

@SpringBootTest
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    void register_puis_login_avecIdentifiantsValides_retourneToken() {
        
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNom("Test");
        registerRequest.setPrenom("User");
        registerRequest.setEmail("test@h2.com");
        registerRequest.setPassword("Password123!@#");
        registerRequest.setTelephone("0600000000");

        authService.register(registerRequest);


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@h2.com");
        loginRequest.setPassword("Password123!@#");

        var response = authService.login(loginRequest);

        // Assert
        assertNotNull(response.getToken());
        assertEquals("CLIENT", response.getRole());
        assertEquals("Test", response.getNom());
    }

    @Test
    void login_avecMauvaisMotDePasse_leveAuthException() {
       
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNom("Testeur");
        registerRequest.setPrenom("deux");
        registerRequest.setEmail("test2@h2.com");
        registerRequest.setPassword("Password123!@#");
        registerRequest.setTelephone("0600000001");

        authService.register(registerRequest);

        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test2@h2.com");
        loginRequest.setPassword("MauvaisMotDePasse!@#");

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.login(loginRequest);
        });
        
        System.out.println("Message reçu : " + exception.getMessage()); 


        assertEquals("Mot de passe incorrect", exception.getMessage());
    }
    
    
}