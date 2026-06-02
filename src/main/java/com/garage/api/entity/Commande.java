package com.garage.api.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Préférable à AUTO
    @Column(name = "id_commande")
    private Long id;

    @Column(name = "date_commande")
    private LocalDate dateCommande;

    private String statut; // "PANIER", "VALIDE", "LIVRE"
    
    @Column(name = "prix_total")
    private double prixTotal;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_mecanicien")
    private Mecanicien mecanicien;
    
    @ManyToMany
    @JoinTable(
        name = "contient",
        joinColumns = @JoinColumn(name = "id_commande"),
        inverseJoinColumns = @JoinColumn(name = "id_produit")
    )
    private List<Produit> produits;
}