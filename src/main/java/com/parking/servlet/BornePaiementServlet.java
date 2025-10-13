package com.parking.servlet;

import com.parking.ejb.TicketService;
import com.parking.model.Ticket;
import com.parking.model.Paiement;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/borne2")
public class BornePaiementServlet extends HttpServlet {

    @EJB
    private TicketService ticketService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/borne2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ticketIdStr = request.getParameter("ticketId");
        String typePaiement = request.getParameter("typePaiement");

        try {
            Long ticketId = Long.parseLong(ticketIdStr);
            Ticket ticket = ticketService.findTicketAvecPaiements(ticketId);
            if(ticket == null) {
                request.setAttribute("message", "Ticket introuvable.");
            } else if(ticket.isPaye()) {
                request.setAttribute("message", "Ticket déjà payé !");
            } else {
                double montant = ticketService.calculerMontant(ticket);
                Paiement paiement = ticketService.payerTicket(ticketId, montant, typePaiement);
                request.setAttribute("ticket", ticket);
                request.setAttribute("paiement", paiement);
                request.setAttribute("message", "Paiement effectué : " + montant + " €");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("message", "ID de ticket invalide.");
        } catch (IllegalStateException e) {
            request.setAttribute("message", e.getMessage());
        }

        request.getRequestDispatcher("/borne2.jsp").forward(request, response);
    }
}
