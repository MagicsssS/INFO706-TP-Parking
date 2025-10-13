<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Borne 1 - Entrée</title>
</head>
<body>
<h1>Ticket créé !</h1>
<p>Numéro du ticket : ${ticket.id}</p>
<p>Date d'entrée : ${ticket.dateEntree}</p>
<a href="borne2?ticketId=${ticket.id}">Payer ce ticket</a>
<a href="index.html">Retour à l'accueil</a>
</body>
</html>
