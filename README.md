# Compte-rendu - TP Parking

## Membres du binôme
- **Membre 1 :** AUBERT Mathys
- **Membre 2 :** WICLAKZ Alain 

---

## Choix d'implantation

- **Technologies utilisées :**  
  - Java EE / Jakarta EE pour les servlets et EJB  
  - JPA / Hibernate pour la persistance  
  - JSP pour l’interface utilisateur  
  - CSS pour le style des pages  

- **Structure de l’application :**  
  - **Borne 1 (Entrée)** : création automatique d’un ticket à l’arrivée.  
  - **Borne 2 (Paiement)** : paiement du ticket et génération d’un justificatif.  
  - **Borne 3 (Sortie)** : vérification du paiement et autorisation de sortie.  

- **Gestion des paiements :**  
  - Chaque paiement contient : montant, type (CB / espèces), et date.  
  - Statut d’un ticket :  
    - Non payé  
    - Paiement expiré  
    - Valide  
    - Déjà sorti  
  - La sortie dépend du statut du ticket.  

- **Durée de validité des paiements :** 15 minutes.  
- **Prix par minute :** 0.02€

---

## Utilisation de l’application

1. **Accueil** 

2. **Borne 1 (Entrée)** :  
   - Cliquer sur "Borne 1 - Entrée".  
   - Un ticket est généré avec un identifiant unique et une date d’entrée.  

3. **Borne 2 (Paiement)** :  
   - Saisir l’ID du ticket.
   - Choisir le type de paiement (CB ou espèces).  
   - Cliquer sur **Payer** pour régler le ticket.  
   - Cliquer sur **Justificatif** pour générer un document détaillant les paiements.  

4. **Borne 3 (Sortie)** :  
   - Saisir l’ID du ticket.  
   - Le système vérifie :  
     - **Paiement non effectué** → message indiquant de payer à la borne 2.  
     - **Paiement expiré** → message invitant à repayer le montant dû depuis le dernier paiement.  
     - **Sortie autorisée** → message confirmant que le véhicule peut quitter le parking.  
