package com.garage.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.api.entity.Prestation;

public interface PrestationRepository extends JpaRepository<Prestation, Long> {
}