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

    private static final long DUREE_MAX_MINUTES = 15; 

    public Ticket creerTicket() {
        Ticket ticket = new Ticket();
        em.persist(ticket);
        return ticket;
    }

    public Ticket findTicket(Long id) {
        return em.find(Ticket.class, id);
    }

    public double calculerMontant(Ticket ticket) {
        Duration duree = Duration.between(ticket.getDateEntree(), LocalDateTime.now());
        return duree.toMinutes() * 0.02;
    }

    public double calculerMontantDepuisDernierPaiement(Ticket ticket) {
        LocalDateTime dateDebut = ticket.getDateEntree();
        Paiement dernier = dernierPaiement(ticket);
        if (dernier != null) {
            dateDebut = dernier.getDatePaiement();
        }
        Duration duree = Duration.between(dateDebut, LocalDateTime.now());
        return duree.toMinutes() * 0.02;
    }

    public Paiement payerTicket(Long ticketId, double montant, String typePaiement) {
        Ticket ticket = em.find(Ticket.class, ticketId);
        if (ticket == null) throw new IllegalArgumentException("Ticket introuvable");
        if (ticket.isSorti()) throw new IllegalStateException("Ticket déjà sorti");

        if (ticket.isPaye() && !paiementExpire(ticketId)) {
            throw new IllegalStateException("Ticket déjà payé et encore valide");
        }

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

    public double montantTotal(Ticket ticket) {
        return ticket.getPaiements().stream().mapToDouble(Paiement::getMontant).sum();
    }

    public Paiement dernierPaiement(Ticket ticket) {
        List<Paiement> paiements = ticket.getPaiements();
        if (paiements.isEmpty()) return null;
        return paiements.get(paiements.size() - 1);
    }

    public boolean paiementExpire(Long ticketId) {
        Ticket ticket = em.find(Ticket.class, ticketId);
        Paiement dernier = dernierPaiement(ticket);
        if(dernier == null) return true; // jamais payé
        Duration delai = Duration.between(dernier.getDatePaiement(), LocalDateTime.now());
        return delai.toMinutes() > DUREE_MAX_MINUTES;
    }

    public String statutTicket(Long ticketId) {
        Ticket ticket = em.find(Ticket.class, ticketId);
        if (ticket == null) return "Inexistant";
        if (ticket.isSorti()) return "Déjà sorti";
        Paiement dernier = dernierPaiement(ticket);
        if (dernier == null) return "Non payé";
        return paiementExpire(ticketId) ? "Paiement expiré" : "Valide";
    }

    public boolean peutSortir(Ticket ticket) {
        if (ticket.isSorti()) return false;
        Paiement dernier = dernierPaiement(ticket);
        if (dernier == null) return false; // jamais payé
        Duration delai = Duration.between(dernier.getDatePaiement(), LocalDateTime.now());
        return delai.toMinutes() <= DUREE_MAX_MINUTES;
    }

    public void sortieTicket(Ticket ticket) {
        ticket.setSorti(true);
        em.merge(ticket);
    }

    public String genererJustificatif(Ticket ticket) {
        List<Paiement> paiements = ticket.getPaiements();
        double total = montantTotal(ticket);

        StringBuilder sb = new StringBuilder();
        sb.append("Ticket n°").append(ticket.getId()).append("\n");
        sb.append("Date d'entrée : ").append(ticket.getDateEntree()).append("\n");
        sb.append("Paiements :\n");

        if (paiements.isEmpty()) {
            sb.append("  Aucun paiement effectué\n");
        } else {
            for (Paiement p : paiements) {
                sb.append("  - Montant : ").append(p.getMontant())
                    .append(" €, Type : ").append(p.getTypePaiement())
                    .append(", Date : ").append(p.getDatePaiement()).append("\n");
            }
        }

        sb.append("Montant total payé : ").append(total).append(" €\n");
        sb.append("Statut : ").append(statutTicket(ticket.getId()));

        return sb.toString();
    }
}
