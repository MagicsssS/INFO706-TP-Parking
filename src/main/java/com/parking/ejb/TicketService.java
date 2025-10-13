package com.parking.ejb;

import com.parking.model.Ticket;
import com.parking.model.Paiement;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class TicketService {

    @PersistenceContext(unitName = "ParkingPU")
    private EntityManager em;

    // Créer un nouveau ticket
    public Ticket creerTicket() {
        Ticket ticket = new Ticket();
        em.persist(ticket);
        return ticket;
    }

    public Ticket findTicketAvecPaiements(Long id) {
        Ticket ticket = em.find(Ticket.class, id);
        // Force le chargement de la collection LAZY
        ticket.getPaiements().size();
        return ticket;
    }

    // Calculer le montant à payer (2 centimes par minute)
    public double calculerMontant(Ticket ticket) {
        Duration duree = Duration.between(ticket.getDateEntree(), LocalDateTime.now());
        return duree.toMinutes() * 0.02;
    }

    // Payer un ticket
    public Paiement payerTicket(Long ticketId, double montant, String typePaiement) throws IllegalStateException {
        Ticket ticket = em.find(Ticket.class, ticketId);
        if(ticket == null) throw new IllegalArgumentException("Ticket introuvable");
        if(ticket.isPaye()) throw new IllegalStateException("Ticket déjà payé");

        Paiement paiement = new Paiement();
        paiement.setMontant(montant);
        paiement.setTypePaiement(typePaiement);
        paiement.setTicket(ticket);

        ticket.getPaiements().add(paiement);
        ticket.setPaye(true);

        em.persist(paiement);
        em.merge(ticket);

        return paiement;
    }

    // Obtenir le montant total payé
    public double montantTotal(Ticket ticket) {
        return ticket.getPaiements().stream().mapToDouble(Paiement::getMontant).sum();
    }
}
