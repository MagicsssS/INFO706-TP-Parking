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
    private boolean paye;
    private LocalDateTime dateSortie;
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Paiement> paiements = new ArrayList<>();

    public Ticket() {
        this.dateEntree = LocalDateTime.now();
        this.paye = false;
        this.dateSortie = null;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateEntree() {
        return dateEntree;
    }

    public boolean isPaye() {
        return paye;
    }

    public void setPaye(boolean paye) {
        this.paye = paye;
    }

    public boolean isSorti() {
        return dateSortie != null;
    }

    public LocalDateTime getDateSortie() { 
        return dateSortie; 
    }
    
    public void setDateSortie(LocalDateTime dateSortie) { 
        this.dateSortie = dateSortie; 
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void ajouterPaiement(Paiement paiement) {
        paiements.add(paiement);
        paiement.setTicket(this);
        this.paye = true;
    }

    public Paiement getDernierPaiement() {
        if (paiements.isEmpty()) return null;
        return paiements.get(paiements.size() - 1);
    }
}
