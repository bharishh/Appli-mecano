package com.garage.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Routes publiques — sans token
                .requestMatchers("/api/garage/auth/**").permitAll()
                .requestMatchers("/api/garage/prestations").permitAll()
                .requestMatchers("/api/garage/produits").permitAll()
                // Routes client — token CLIENT
                .requestMatchers("/api/garage/rendezvous").hasRole("CLIENT")
                // Routes mécanicien — token MECANICIEN
                .requestMatchers("/api/garage/rendezvous/**").hasRole("MECANICIEN")
                .requestMatchers("/api/garage/produits/**").hasRole("MECANICIEN")
                .requestMatchers("/api/garage/prestations/**").hasRole("MECANICIEN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}