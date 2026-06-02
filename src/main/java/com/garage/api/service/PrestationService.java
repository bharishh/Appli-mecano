package com.garage.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.garage.api.entity.Prestation;
import com.garage.api.repository.PrestationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrestationService {

    private final PrestationRepository prestationRepository;

    // Récupérer toutes les prestations (visible par tout le monde)
    public List<Prestation> getAllPrestations() {
        return prestationRepository.findAll();
    }

    // Ajouter une prestation (admin seulement)
    public Prestation ajouterPrestation(Prestation prestation) {
        return prestationRepository.save(prestation);
    }

    // Modifier une prestation (admin seulement)
    public Prestation modifierPrestation(Long id, Prestation prestation) {
        Prestation existing = prestationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestation introuvable"));
        existing.setLibelle(prestation.getLibelle());
        existing.setDescription(prestation.getDescription());
        existing.setPrix(prestation.getPrix());
        existing.setDuree(prestation.getDuree());
        return prestationRepository.save(existing);
    }

    // Supprimer une prestation (admin seulement)
    public void supprimerPrestation(Long id) {
        prestationRepository.deleteById(id);
    }
}