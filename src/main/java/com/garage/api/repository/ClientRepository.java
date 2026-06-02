package com.garage.api.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.api.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client>findByEmail(String email);
}
