package com.garage.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage.api.entity.Prestation;
import com.garage.api.service.PrestationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/garage/prestations")
@RequiredArgsConstructor
public class PrestationController {

    private final PrestationService prestationService;

    // Public — tout le monde peut voir les prestations
    @GetMapping
    public ResponseEntity<List<Prestation>> getAllPrestations() {
        return ResponseEntity.ok(prestationService.getAllPrestations());
    }

    // Admin seulement
    @PostMapping
    public ResponseEntity<Prestation> ajouterPrestation(@RequestBody Prestation prestation) {
        return ResponseEntity.ok(prestationService.ajouterPrestation(prestation));
    }

    // Admin seulement
    @PutMapping("/{id}")
    public ResponseEntity<Prestation> modifierPrestation(@PathVariable Long id,
                                                          @RequestBody Prestation prestation) {
        return ResponseEntity.ok(prestationService.modifierPrestation(id, prestation));
    }

    // Admin seulement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPrestation(@PathVariable Long id) {
        prestationService.supprimerPrestation(id);
        return ResponseEntity.noContent().build();
    }
}