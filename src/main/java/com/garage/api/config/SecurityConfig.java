package com.garage.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Prise en compte de la configuration CORS (indispensable pour ton Front-End JS)
            .cors(Customizer.withDefaults()) 
            
            // 2. Désactivation du CSRF (obligatoire pour autoriser les requêtes POST/PUT/DELETE sans token)
            .csrf(csrf -> csrf.disable())
            
            // 3. Gestion des autorisations des routes
            .authorizeHttpRequests(auth -> auth
                // 🔓 EN PHASE DE DEV : On ouvre absolument TOUT ce qui commence par /api/garage/
                .requestMatchers("/api/garage/**").permitAll()
                
                // Tout le reste (si jamais tu as d'autres préfixes) demande une connexion
                .anyRequest().authenticated()
            );

        return http.build();
    }
}