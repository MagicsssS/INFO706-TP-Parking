<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Borne 3 - Sortie</title>
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
        h2 { text-align: center; color: #333; }
        form { display: flex; flex-direction: column; gap: 10px; margin-top: 20px; }
        input[type="text"] { padding: 8px; border-radius: 4px; border: 1px solid #ccc; }
        button { padding: 10px; border-radius: 4px; border: none; background: #dc3545; color: white; cursor: pointer; font-size: 16px; }
        button:hover { background: #c82333; }
        p { margin-top: 15px; padding: 10px; background: #f8f9fa; border-radius: 4px; border: 1px solid #ddd; }
        a { display: inline-block; margin-top: 15px; text-decoration: none; color: #007bff; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container">
    <h2>Borne 3 - Sortie</h2>
    <form method="post">
        <label>ID du ticket :</label>
        <input type="text" name="ticketId" required>
        <button type="submit">Sortir</button>
    </form>

    <c:if test="${not empty message}">
        <p>${message}</p>
    </c:if>

    <a href="index.jsp">Retour</a>
</div>
</body>
</html>
