package com.parking.servlet;

import com.parking.ejb.TicketService;
import com.parking.model.Ticket;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@WebServlet("/borne1")
public class Borne1Servlet extends HttpServlet {

    @EJB
    private TicketService ticketService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Ticket ticket = ticketService.creerTicket();
        LocalDateTime dateEntree = ticket.getDateEntree();
        Date dateEntreeUtil = Date.from(dateEntree.atZone(ZoneId.systemDefault()).toInstant());
        request.setAttribute("dateEntreeUtil", dateEntreeUtil);
        request.setAttribute("ticket", ticket);
        request.getRequestDispatcher("/borne1.jsp").forward(request, response);
        
    }
}
