package com.parking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateEntree;

    private LocalDateTime dateSortie;

    private boolean paye;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Paiement> paiements = new ArrayList<>();

    public Ticket() {
        this.dateEntree = LocalDateTime.now();
        this.paye = false;
    }

    public Long getId() { return id; }
    public LocalDateTime getDateEntree() { return dateEntree; }
    public LocalDateTime getDateSortie() { return dateSortie; }
    public void setDateSortie(LocalDateTime dateSortie) { this.dateSortie = dateSortie; }
    public boolean isPaye() { return paye; }
    public void setPaye(boolean paye) { this.paye = paye; }
    public List<Paiement> getPaiements() { return paiements; }

    public double montantTotal() {
        return paiements.stream().mapToDouble(Paiement::getMontant).sum();
    }
}
