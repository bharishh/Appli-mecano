package com.garage.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.api.entity.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Long>{
	// Pour le mécanicien : afficher les commandes à traiter (statut "EN_ATTENTE" ou "PAYE")
    List<Commande> findByStatut(String statut);

    // Pour le client : voir son historique d'achats
    List<Commande> findByClientId(Long clientId);
}
