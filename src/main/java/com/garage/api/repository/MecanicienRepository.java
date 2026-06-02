package com.garage.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.api.entity.Mecanicien;

public interface MecanicienRepository extends JpaRepository<Mecanicien, Long> {

    Optional<Mecanicien> findByEmail(String email);
}