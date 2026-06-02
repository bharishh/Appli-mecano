package com.garage.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class RendezVousRequest {

	// Infos du RDV
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private Long idPrestation;
    private Long idClient;

    // Infos du Véhicule
    private String marque;
    private String modele;
    private String immatriculation;
    private int annee;
    private String typeCarburant;
    private int kilometrage;
}
