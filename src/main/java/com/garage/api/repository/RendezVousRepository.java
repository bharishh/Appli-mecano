package com.garage.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.api.entity.RendezVous;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long>{

	// Pour le planning du mécanicien : voir les RDV d'un jour précis
    List<RendezVous> findByDateRdv(LocalDate dateRdv);

    // Pour l'historique du client : voir tous ses RDV
    List<RendezVous> findByClientId(Long clientId);

    // Pour éviter les doublons : vérifier si un horaire est déjà pris
    boolean existsByDateRdvAndHeureRdv(LocalDate dateRdv, java.time.LocalTime heureRdv);
}
