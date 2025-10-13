<h2>Paiement du ticket</h2>
<c:if test="${not empty message}">
    <p>${message}</p>
</c:if>

<form method="post" action="borne2">
    <label>ID du ticket :</label>
    <input type="text" name="ticketId" />
    <br/>
    <label>Type de paiement :</label>
    <select name="typePaiement">
        <option value="CB">CB</option>
        <option value="Espèces">Espèces</option>
    </select>
    <br/>
    <button type="submit">Payer</button>
</form>

<c:if test="${not empty ticket}">
    <h3>Justificatif :</h3>
    <p>Numéro du ticket : ${ticket.id}</p>
    <p>Date d'entrée : ${ticket.dateEntree}</p>
    <p>Dernier paiement : ${paiement.datePaiement}</p>
    <p>Montant total payé : ${ticket.montantTotal()}</p>
</c:if>
