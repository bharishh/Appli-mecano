package com.garage.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garage.api.entity.Client;
import com.garage.api.entity.Commande;
import com.garage.api.entity.Mecanicien;
import com.garage.api.entity.Produit;
import com.garage.api.repository.ClientRepository;
import com.garage.api.repository.CommandeRepository;
import com.garage.api.repository.MecanicienRepository;
import com.garage.api.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final ProduitRepository produitRepository;
    private final MecanicienRepository mecanicienRepository;

    @Transactional
    public Commande passerCommande(Long clientId, List<Long> idsProduits) {

        // 1. Récupérer le client
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        // 2. Récupérer le mécanicien par défaut
        Mecanicien mecanicien = mecanicienRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Mécanicien introuvable"));

        // 3. Récupérer les produits
        List<Produit> produits = produitRepository.findAllById(idsProduits);
        if (produits.isEmpty())
            throw new RuntimeException("Aucun produit trouvé");

        // 4. Vérifier le stock et calculer le prix total
        double prixTotal = 0;
        for (Produit p : produits) {
            if (p.getQuantiteStock() <= 0)
                throw new RuntimeException("Produit en rupture de stock : " + p.getNom());
            prixTotal += p.getPrix();
            // Décrémenter le stock
            p.setQuantiteStock(p.getQuantiteStock() - 1);
            produitRepository.save(p);
        }

        // 5. Créer la commande
        Commande commande = Commande.builder()
                .dateCommande(LocalDate.now())
                .statut("VALIDE")
                .prixTotal(prixTotal)
                .client(client)
                .mecanicien(mecanicien)
                .produits(produits)
                .build();

        return commandeRepository.save(commande);
    }

    // Commandes d'un client
    public List<Commande> getCommandesClient(Long clientId) {
        return commandeRepository.findByClientId(clientId);
    }

    // Toutes les commandes (admin)
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    // Changer statut (admin)
    public Commande changerStatut(Long id, String statut) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        commande.setStatut(statut);
        return commandeRepository.save(commande);
    }
}