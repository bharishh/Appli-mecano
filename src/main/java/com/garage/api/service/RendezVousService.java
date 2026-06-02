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
        
        // 1. Vérifier si le créneau est déjà pris
        if (rendezVousRepository.existsByDateRdvAndHeureRdv(request.getDateRdv(), request.getHeureRdv())) {
            throw new RuntimeException("Ce créneau horaire est déjà réservé.");
        }

        // 2. Récupérer le client
        Client client = clientRepository.findById(request.getIdClient())
                .orElseThrow(() -> new RuntimeException("Client introuvable."));

        // 3. Récupérer la prestation
        Prestation prestation = prestationRepository.findById(request.getIdPrestation())
                .orElseThrow(() -> new RuntimeException("Prestation introuvable."));

        // 4. Récupérer le mécanicien par défaut (ID 1L) obligatoire selon le MLD
        Mecanicien mecanicienParDefaut = mecanicienRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Mécanicien par défaut introuvable en BDD."));
        
        // 5. Créer et enregistrer le véhicule lié au client (génère l'id_vehicule)
        Vehicule vehicule = Vehicule.builder()
                .marque(request.getMarque())
                .modele(request.getModele())
                .immatriculation(request.getImmatriculation())
                .annee(request.getAnnee())
                .typeCarburant(request.getTypeCarburant())
                .kilometrage(request.getKilometrage())
                .client(client) // Clé étrangère #id_client rattachée
                .build();
        
        vehicule = vehiculeRepository.save(vehicule);

        // 6. Créer et enregistrer le Rendez-vous avec TOUTES ses clés étrangères du MLD
        RendezVous rdv = RendezVous.builder()
                .dateRdv(request.getDateRdv())
                .heureRdv(request.getHeureRdv())
                .statut("EN_ATTENTE")
                .rappelEnvoye(false)
                .client(client)           // #id_client_fk
                .vehicule(vehicule)       // #id_vehicule
                .prestation(prestation)   // #id_service_fk
                .mecanicien(mecanicienParDefaut) // #id_mecanicien_fk (Correction apportée ici)
                .build();

        RendezVous rdvEnregistre = rendezVousRepository.save(rdv);

        // TODO: Déclencher l'envoi du mail de confirmation ici plus tard

        return rdvEnregistre;
    }

    // Pour le planning du mécanicien (Admin)
    public List<RendezVous> getPlanningParJour(LocalDate date) {
        return rendezVousRepository.findByDateRdv(date);
    }

    // Annuler ou modifier le statut d'un RDV (Mécano ou Client)
    public RendezVous changerStatutRDV(Long idRdv, String nouveauStatut) {
        RendezVous rdv = rendezVousRepository.findById(idRdv)
                .orElseThrow(() -> new RuntimeException("Rendez-vous introuvable."));
        rdv.setStatut(nouveauStatut);
        return rendezVousRepository.save(rdv);
    }
}