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
@CrossOrigin(origins = "*")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    
    @PostMapping
    public ResponseEntity<RendezVous> creerRendezVous(@RequestBody RendezVousRequest request) {
        return ResponseEntity.ok(rendezVousService.prendreRendezVous(request));
    }

 
    @GetMapping("/planning")
    public ResponseEntity<List<RendezVous>> voirPlanning(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(rendezVousService.getPlanningParJour(localDate));
    }

  
    @PutMapping("/{id}/statut")
    public ResponseEntity<RendezVous> modifierStatut(@PathVariable Long id, @RequestParam String statut) {
        return ResponseEntity.ok(rendezVousService.changerStatutRDV(id, statut));
    }
    
    @GetMapping
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        return ResponseEntity.ok(rendezVousService.getAllRendezVous());
    }

}