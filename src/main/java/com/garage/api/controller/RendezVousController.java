package com.garage.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.garage.api.dto.RendezVousRequest;
import com.garage.api.entity.RendezVous;
import com.garage.api.service.RendezVousService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/garage/rendezvous")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permet à ton Front-End HTML/JS de requêter le Back-End sans blocage CORS
public class RendezVousController {

    private final RendezVousService rendezVousService;

    // Route pour le client : Prendre RDV
    @PostMapping
    public ResponseEntity<RendezVous> creerRendezVous(@RequestBody RendezVousRequest request) {
        return ResponseEntity.ok(rendezVousService.prendreRendezVous(request));
    }

    // Route pour le mécanicien : Voir le planning d'un jour précis
    // Exemple d'appel : /api/garage/rendezvous/planning?date=2026-06-15
    @GetMapping("/planning")
    public ResponseEntity<List<RendezVous>> voirPlanning(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(rendezVousService.getPlanningParJour(localDate));
    }

    // Route pour modifier le statut (Valider, Annuler...)
    @PutMapping("/{id}/statut")
    public ResponseEntity<RendezVous> modifierStatut(@PathVariable Long id, @RequestParam String statut) {
        return ResponseEntity.ok(rendezVousService.changerStatutRDV(id, statut));
    }
}