package com.parking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePaiement;
    private double montant;
    private String typePaiement;

    @ManyToOne
    private Ticket ticket;

    public Paiement() {
        this.datePaiement = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
