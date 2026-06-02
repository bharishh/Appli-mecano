package com.garage.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.garage.api.entity.Produit;
import com.garage.api.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    // Tous les produits (public)
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // Produits par type ex: "Pneu"
    public List<Produit> getProduitsByType(String type) {
        return produitRepository.findByType(type);
    }

    // Produits stock faible (admin)
    public List<Produit> getStockFaible(int seuil) {
        return produitRepository.findByQuantiteStockLessThan(seuil);
    }

    // Ajouter un produit (admin)
    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    // Modifier un produit (admin)
    public Produit modifierProduit(Long id, Produit produit) {
        Produit existing = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        existing.setNom(produit.getNom());
        existing.setType(produit.getType());
        existing.setPrix(produit.getPrix());
        existing.setQuantiteStock(produit.getQuantiteStock());
        return produitRepository.save(existing);
    }

    // Supprimer un produit (admin)
    public void supprimerProduit(Long id) {
        produitRepository.deleteById(id);
    }
}