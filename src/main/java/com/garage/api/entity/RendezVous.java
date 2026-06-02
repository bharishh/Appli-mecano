package com.garage.api.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rendez_vous")
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rdv")
    private Long id;

    @Column(name = "date_rdv", nullable = false)
    private LocalDate dateRdv;

    @Column(name = "heure_rdv", nullable = false)
    private LocalTime heureRdv;

    private String statut; // "EN_ATTENTE", "VALIDE", "ANNULE"
    
    @Column(name = "rappel_envoye")
    private boolean rappelEnvoye;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_vehicule")
    private Vehicule vehicule;
    @ManyToOne
    @JoinColumn(name = "id_mecanicien") // 👈 Doit correspondre à la colonne mecanicien
    private Mecanicien mecanicien;

    @ManyToOne
    @JoinColumn(name = "id_service")
    private Prestation prestation; // (Appelée 'Service' dans ton MCD)
}