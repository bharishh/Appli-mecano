package com.garage.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.api.entity.Vehicule;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
	 List<Vehicule> findByClientId(Long clientId);
}
