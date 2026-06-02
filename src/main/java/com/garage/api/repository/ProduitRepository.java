package com.garage.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.api.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
	
	
	// Trouver les produits par type (ex: "Pneu")
    List<Produit> findByType(String type);

    // Pour le tableau de bord du mécanicien : voir les produits dont le stock est inférieur à un seuil
    List<Produit> findByQuantiteStockLessThan(int seuil);
}