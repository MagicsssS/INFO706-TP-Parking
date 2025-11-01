package com.parking.servlet;

import com.parking.ejb.TicketService;
import com.parking.model.Ticket;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/borne3")
public class Borne3Servlet extends HttpServlet {

    @EJB
    private TicketService ticketService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("borne3.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = "";
        try {
            Long ticketId = Long.parseLong(request.getParameter("ticketId"));
            Ticket ticket = ticketService.findTicket(ticketId);

            if (ticket == null) {
                message = "Ticket introuvable. Veuillez vous rendre à la borne 1.";
            } else {
                // On récupère le statut précis du ticket
                String statut = ticketService.statutTicket(ticketId);

                switch (statut) {
                    case "Déjà sorti":
                        message = "Ticket déjà sorti.";
                        break;
                    case "Non payé":
                        message = "Paiement non effectué. Veuillez régler à la borne 2.";
                        break;
                    case "Paiement expiré":
                        message = "Paiement expiré. Veuillez repayer le montant dû à la borne 2.";
                        break;
                    case "Valide":
                        ticketService.sortieTicket(ticket);
                        message = "Sortie autorisée. Bon retour !";
                        break;
                    default:
                        message = "Erreur inconnue.";
                        break;
                }
            }
        } catch (NumberFormatException e) {
            message = "ID de ticket invalide.";
        } catch (Exception e) {
            message = "Erreur serveur : " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.getRequestDispatcher("borne3.jsp").forward(request, response);
    }
}
