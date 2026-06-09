package com.garage.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garage.api.dto.RendezVousRequest;
import com.garage.api.entity.Client;
import com.garage.api.entity.Mecanicien;
import com.garage.api.entity.Prestation;
import com.garage.api.entity.RendezVous;
import com.garage.api.entity.Vehicule;
import com.garage.api.repository.ClientRepository;
import com.garage.api.repository.MecanicienRepository;
import com.garage.api.repository.PrestationRepository;
import com.garage.api.repository.RendezVousRepository;
import com.garage.api.repository.VehiculeRepository;
import com.garage.api.utils.XssUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final ClientRepository clientRepository;
    private final PrestationRepository prestationRepository;
    private final VehiculeRepository vehiculeRepository;
    private final MecanicienRepository mecanicienRepository;

    @Transactional
    public RendezVous prendreRendezVous(RendezVousRequest request) {
        
    	// Validation XSS
        if (!XssUtils.isValidName(request.getMarque()))
            throw new RuntimeException("Marque invalide");
        if (!XssUtils.isValidName(request.getModele()))
            throw new RuntimeException("Modèle invalide");
        if (!XssUtils.isValidImmatriculation(request.getImmatriculation()))
            throw new RuntimeException("Immatriculation invalide (format : AB-123-CD)");

        
 
        if (rendezVousRepository.existsByDateRdvAndHeureRdv(request.getDateRdv(), request.getHeureRdv())) {
            throw new RuntimeException("Ce créneau horaire est déjà réservé.");
        }

  
        Client client = clientRepository.findById(request.getIdClient())
                .orElseThrow(() -> new RuntimeException("Client introuvable."));

 
        Prestation prestation = prestationRepository.findById(request.getIdPrestation())
                .orElseThrow(() -> new RuntimeException("Prestation introuvable."));

  
        Mecanicien mecanicienParDefaut = mecanicienRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Mécanicien par défaut introuvable en BDD."));
        
        
        Vehicule vehicule = Vehicule.builder()
                .marque(XssUtils.sanitize(request.getMarque()))
                .modele(XssUtils.sanitize(request.getModele()))
                .immatriculation(request.getImmatriculation().toUpperCase().trim())
                .annee(request.getAnnee())
                .typeCarburant(XssUtils.sanitize(request.getTypeCarburant()))
                .kilometrage(request.getKilometrage())
                .client(client)
                .build();
        vehicule = vehiculeRepository.save(vehicule);

       
        RendezVous rdv = RendezVous.builder()
                .dateRdv(request.getDateRdv())
                .heureRdv(request.getHeureRdv())
                .statut("EN_ATTENTE")
                .rappelEnvoye(false)
                .client(client)           
                .vehicule(vehicule)      
                .prestation(prestation)   
                .mecanicien(mecanicienParDefaut) 
                .build();

        RendezVous rdvEnregistre = rendezVousRepository.save(rdv);

        // TODO: Déclencher l'envoi du mail de confirmation ici plus tard

        return rdvEnregistre;
    }
    
    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    // Pour le planning du mécanicien (Admin)
    public List<RendezVous> getPlanningParJour(LocalDate date) {
        return rendezVousRepository.findByDateRdv(date);
    }

    
    public RendezVous changerStatutRDV(Long idRdv, String nouveauStatut) {
        RendezVous rdv = rendezVousRepository.findById(idRdv)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable."));
        rdv.setStatut(nouveauStatut);
        return rendezVousRepository.save(rdv);
    }
}