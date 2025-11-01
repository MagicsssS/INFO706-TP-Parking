package com.parking.servlet;

import com.parking.ejb.TicketService;
import com.parking.model.Ticket;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/borne2")
public class Borne2Servlet extends HttpServlet {

    @EJB
    private TicketService ticketService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("borne2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String typePaiement = request.getParameter("typePaiement");
        String message = "";
        String justificatif = null;

        try {
            Long id = Long.parseLong(request.getParameter("ticketId"));
            Ticket ticket = ticketService.findTicket(id);

            if (ticket == null) {
                message = "Ticket introuvable.";
            } else {
                switch (action) {
                    case "payer":
                        try {
                            double montant;
                            if(ticket.isPaye() && ticketService.paiementExpire(ticket.getId())) {
                                montant = ticketService.calculerMontantDepuisDernierPaiement(ticket);
                            } else {
                                montant = ticketService.calculerMontant(ticket);
                            }
                            ticketService.payerTicket(id, montant, typePaiement);
                            message = String.format("Paiement effectué : %.2f € (%s)", montant, typePaiement);
                        } catch (IllegalStateException e) {
                            message = e.getMessage();
                        }
                        break;

                    case "justificatif":
                        justificatif = ticketService.genererJustificatif(ticket);
                        break;

                    default:
                        message = "Action inconnue.";
                        break;
                }
            }
        } catch (NumberFormatException e) {
            message = "ID de ticket invalide.";
        } catch (Exception e) {
            message = "Erreur serveur : " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.setAttribute("justificatif", justificatif);
        request.getRequestDispatcher("borne2.jsp").forward(request, response);
    }
}
