package com.parking.ejb;

import com.parking.model.Ticket;
import com.parking.model.Paiement;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Stateless
public class TicketService {

    @PersistenceContext(unitName = "ParkingPU")
    private EntityManager em;

    private static final long DUREE_MAX_MINUTES = 15;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
        Paiement dernier = ticket.getDernierPaiement();
        if (dernier != null) dateDebut = dernier.getDatePaiement();
        Duration duree = Duration.between(dateDebut, LocalDateTime.now());
        return duree.toMinutes() * 0.02;
    }

    public Paiement payerTicket(Long ticketId, double montant, String typePaiement) {
        Ticket ticket = em.find(Ticket.class, ticketId);
        if (ticket == null) throw new IllegalArgumentException("Ticket introuvable");
        if (ticket.getDateSortie() != null) throw new IllegalStateException("Ticket déjà sorti");

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
        if (dernier == null) return true; // jamais payé
        Duration delai = Duration.between(dernier.getDatePaiement(), LocalDateTime.now());
        return delai.toMinutes() > DUREE_MAX_MINUTES;
    }

    public String statutTicket(Long ticketId) {
        Ticket ticket = em.find(Ticket.class, ticketId);
        if (ticket == null) return "Inexistant";
        if (ticket.getDateSortie() != null) return "Déjà sorti";
        Paiement dernier = dernierPaiement(ticket);
        if (dernier == null) return "Non payé";
        return paiementExpire(ticketId) ? "Paiement expiré" : "Valide";
    }

    public boolean peutSortir(Ticket ticket) {
        if (ticket.getDateSortie() != null) return false;
        Paiement dernier = dernierPaiement(ticket);
        if (dernier == null) return false;
        Duration delai = Duration.between(dernier.getDatePaiement(), LocalDateTime.now());
        return delai.toMinutes() <= DUREE_MAX_MINUTES;
    }

    public void sortieTicket(Ticket ticket) {
        ticket.setDateSortie(LocalDateTime.now());
        em.merge(ticket);
    }

    public String genererJustificatif(Ticket ticket) {
        Paiement dernier = ticket.getDernierPaiement();
        double total = montantTotal(ticket);
        String dateDernierPaiement = (dernier == null) ? "Aucun paiement" : dernier.getDatePaiement().format(FORMATTER);

        StringBuilder sb = new StringBuilder();
        sb.append("Ticket n°").append(ticket.getId()).append("\n");
        sb.append("Date d'entrée : ").append(ticket.getDateEntree().format(FORMATTER)).append("\n");
        sb.append("Date dernier paiement : ").append(dateDernierPaiement).append("\n");
        sb.append("Montant total payé : ").append(total).append(" €");

        return sb.toString();
    }
}
