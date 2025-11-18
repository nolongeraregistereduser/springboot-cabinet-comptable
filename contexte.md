Contexte du projet
Le Cabinet Comptable Al Amane gère actuellement 35 sociétés clientes de différentes tailles (TPE, PME) et fait face à plusieurs problématiques critiques :

Problèmes Identifiés
Perte et Détérioration de Documents
40% des clients conservent uniquement des documents papier
Plusieurs cas de perte de factures lors de contrôles fiscaux
Documents détériorés par le temps, l'humidité ou accidents
Non-Conformité Légale
Risque de pénalités de 50 000 DH par exercice (Article 211 CGI)
Difficulté à respecter l'obligation de conservation de 10 ans (Loi N° 9-88)
Le cabinet souhaite une solution permettant de :

Assurer la conformité légale avec la réglementation marocaine
Centraliser tous les documents de ses clients dans un seul système
Sécuriser les documents contre tout risque de perte, destruction ou détérioration
Faciliter l'échange de documents entre clients et comptables
Faciliter l'accès et la recherche rapide des pièces justificatives
Fonctionnalités:
Authentification & Sécurité
Connexion par email/mot de passe
Tokens JWT avec expiration 24h
Séparation des rôles (SOCIETE/COMPTABLE)
Gestion Documents - Côté Société
Upload de pièces justificatives(PDF, JPG, PNG jusqu'à 10MB)
Saisie métadonnées : n° pièce, type, date, montant, fournisseur
Consultation de tous ses pièces justificatives par exercice comptable
Visualisation du statut (En Attente/Validé/Rejeté)
Gestion Documents - Côté Comptable
Liste de tous les pièces justificatives en attente (toutes sociétés et exercice comptable)
Validation de pièces justificatives avec commentaire optionnel
Rejet de pièces justificatives avec motif obligatoire
Vue par société cliente
Architecture Technique:
Stack Technologique Retenue:

Backend : Spring Boot 3.4.0+ (REST API)
Sécurité: Spring Security 6
Authentication: Stateless (avec JWT), UserDetailsService
Base de données : H2 ,
Storage: en local, AWS Storage, cloudinary, Destroy(Implémenter une méthode qui sera appelée automatiquement à l'arrêt de l'application pour supprimer tous les fichiers uploadés.)
Test unitaire: Junit 5 et Mockito
Structure de données:
Société: Raison sociale, ICE (Identifiant Commun de l'Entreprise), Adresse, Téléphone, Email de contact
Utilisateurs: Email (identifiant unique), Mot de passe (crypté BCrypt), Nom complet, Rôle (SOCIETE ou COMPTABLE), Société rattachée (si SOCIETE), Statut actif/inactif, Date de création
Document: Numéro de pièce, Type (Facture d’achat, Facture de vente, Ticket de caisse, Relevé bancaire), Catégorie comptable, Date de la pièce, Montant, Fournisseur, Fichier de pièce, Statut (EN_ATTENTE, VALIDE, REJETE), Date de validation, Commentaire du comptable, Société propriétaire, Dates de création/modification
Test unitaire:
Développement des tests unitaires pour la couche Repository
Dockerization de l’application:
Création du Dockerfile
Construction de l'image Docker
Exécution du conteneur à partir de l’image créée
pusher l'image créée vers Docker Hub
CI avec GITHUB ACTIONS (1 workflow) optionnel:
Build
Docker Build & Push
Configurer les Secrets GitHub