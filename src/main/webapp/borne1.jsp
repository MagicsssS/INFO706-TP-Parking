<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Borne 1 - Entrée</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f0f2f5; padding: 20px; }
        .container {
            max-width: 500px;
            margin: 50px auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 { text-align: center; color: #333; }
        p { font-size: 16px; margin: 10px 0; }
        a {
            display: inline-block;
            margin: 10px 5px;
            padding: 10px 15px;
            text-decoration: none;
            background: #007bff;
            color: white;
            border-radius: 4px;
        }
        a:hover { background: #0056b3; }
    </style>
</head>
<body>
<div class="container">
    <h1>Ticket créé !</h1>
    <p>Numéro du ticket : ${ticket.id}</p>
    <p>Date d'entrée : 
        <fmt:formatDate value="${dateEntreeUtil}" pattern="dd/MM/yyyy HH:mm:ss"/>
    </p>
    <a href="borne2?ticketId=${ticket.id}">Payer ce ticket</a>
    <a href="index.jsp">Retour à l'accueil</a>
</div>
</body>
</html>
