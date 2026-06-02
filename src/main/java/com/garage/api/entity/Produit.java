package com.garage.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long id;

    private String nom;
    private String type; // Exemple: "Pneu"
    private double prix;

    @Column(name = "quantite_stock")
    private int quantiteStock;
}