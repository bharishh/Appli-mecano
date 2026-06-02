package com.garage.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	
	public Client register(RegisterRequest request)
	{
		if(clientRepository.findByEmail(request.getEmail()).isPresent())
			throw new AuthException("Email déjà utilisé");
		
		Client client = Client.builder()
				.nom(request.getNom())
				.prenom(request.getPrenom())
				.email(request.getEmail())
				.telephone(request.getTelephone())
				.password(passwordEncoder.encode(request.getPassword()))
				.build();
		
		return clientRepository.save(client);
	}
	
	
	public Client login(LoginRequest request)
	{
		
		
		Client client = clientRepository.findByEmail(request.getEmail())
		        .orElseThrow(() -> new AuthException("email introuvable"));

		if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
		    throw new AuthException("mot de passe incorrect");
		}

		return client;
	
	}
	
	 public Mecanicien loginMecanicien(LoginRequest request) {
	        Mecanicien meca = mecanicienRepository.findByEmail(request.getEmail())
	                .orElseThrow(() -> new AuthException("Email introuvable"));

	        if (!passwordEncoder.matches(request.getPassword(), meca.getPassword()))
	            throw new AuthException("Mot de passe incorrect");

	        return meca;
	    }
}
