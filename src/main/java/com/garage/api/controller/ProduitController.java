package com.garage.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage.api.entity.Produit;
import com.garage.api.service.ProduitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/garage/produits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProduitController {

    private final ProduitService produitService;

    // Public — tout le monde peut voir les produits
    @GetMapping
    public ResponseEntity<List<Produit>> getAllProduits() {
        return ResponseEntity.ok(produitService.getAllProduits());
    }

    // Filtrer par type
    @GetMapping("/type")
    public ResponseEntity<List<Produit>> getProduitsByType(@RequestParam String type) {
        return ResponseEntity.ok(produitService.getProduitsByType(type));
    }

    // Stock faible (admin)
    @GetMapping("/stock-faible")
    public ResponseEntity<List<Produit>> getStockFaible(@RequestParam int seuil) {
        return ResponseEntity.ok(produitService.getStockFaible(seuil));
    }

    // Ajouter un produit (admin)
    @PostMapping
    public ResponseEntity<Produit> ajouterProduit(@RequestBody Produit produit) {
        return ResponseEntity.ok(produitService.ajouterProduit(produit));
    }

    // Modifier un produit (admin)
    @PutMapping("/{id}")
    public ResponseEntity<Produit> modifierProduit(@PathVariable Long id,
                                                    @RequestBody Produit produit) {
        return ResponseEntity.ok(produitService.modifierProduit(id, produit));
    }

    // Supprimer un produit (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable Long id) {
        produitService.supprimerProduit(id);
        return ResponseEntity.noContent().build();
    }
}