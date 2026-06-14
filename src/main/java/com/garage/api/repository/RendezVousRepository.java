package com.garage.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.garage.api.entity.RendezVous;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long>{


    List<RendezVous> findByDateRdv(LocalDate dateRdv);


    List<RendezVous> findByClientId(Long clientId);
    
    boolean existsByDateRdvAndHeureRdv(LocalDate dateRdv, java.time.LocalTime heureRdv);
}
