# 🧾 ComptableController API Testing Guide

This guide provides exact request formats for testing all ComptableController endpoints.

**⚠️ Important:** All these endpoints require **COMPTABLE** role. Login as a comptable user first!

---

## 📋 Table of Contents

1. [Login as Comptable](#1-login-as-comptable)
2. [Get Pending Documents](#2-get-pending-documents)
3. [Validate Document](#3-validate-document)
4. [Reject Document](#4-reject-document)
5. [Get Documents by Société](#5-get-documents-by-société)
6. [Get Documents by Exercice](#6-get-documents-by-exercice)

---

## 1. Login as Comptable

### **Endpoint:** `POST /api/auth/login`

### **Request Body (JSON):**
```json
{
  "email": "mohamed@comptable.com",
  "password": "123456789"
}
```

### **Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "mohamed@comptable.com",
  "nomComplet": "Mohamed Alami",
  "role": "COMPTABLE",
  "societeId": null,
  "societeRaisonSociale": null
}
```

**Save the `token` for all subsequent requests!**

---

## 2. Get Pending Documents

### **Endpoint:** `GET /api/comptable/documents/en-attente`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Request Body:**
**None** (GET request)

### **URL:**
```
GET http://localhost:8080/api/comptable/documents/en-attente
```

### **Response Example:**
```json
[
  {
    "id": 1,
    "numeroPiece": "FAC-2024-001",
    "type": "FACTURE_ACHAT",
    "categorieComptable": "Achats de marchandises",
    "datePiece": "2024-01-15",
    "montant": 1500.00,
    "fournisseur": "Fournisseur ABC SARL",
    "nomFichierOriginal": "facture.pdf",
    "statut": "EN_ATTENTE",
    "commentaireComptable": null,
    "dateValidation": null,
    "createdAt": "2024-01-20T10:30:00",
    "updatedAt": null,
    "societeId": 1,
    "societeRaisonSociale": "TechnoMaroc SARL"
  },
  {
    "id": 2,
    "numeroPiece": "FAC-2024-002",
    "type": "FACTURE_VENTE",
    "categorieComptable": "Ventes de produits",
    "datePiece": "2024-01-16",
    "montant": 2500.00,
    "fournisseur": "Client XYZ",
    "nomFichierOriginal": "vente.pdf",
    "statut": "EN_ATTENTE",
    "commentaireComptable": null,
    "dateValidation": null,
    "createdAt": "2024-01-17T09:15:00",
    "updatedAt": null,
    "societeId": 2,
    "societeRaisonSociale": "Atlas Import Export"
  }
]
```

**Note:** Returns all documents with status `EN_ATTENTE` from all sociétés.

---

## 3. Validate Document

### **Endpoint:** `POST /api/comptable/documents/{id}/valider`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
Content-Type: application/json
```

### **Request Body (JSON) - Optional:**
```json
{
  "commentaire": "Document validé après vérification"
}
```

**OR** (commentaire is optional for validation):
```json
{}
```

**OR** (no body at all - commentaire will be null):
```
(empty body)
```

### **URL Example:**
```
POST http://localhost:8080/api/comptable/documents/1/valider
```

### **Response Example:**
```json
{
  "id": 1,
  "numeroPiece": "FAC-2024-001",
  "type": "FACTURE_ACHAT",
  "categorieComptable": "Achats de marchandises",
  "datePiece": "2024-01-15",
  "montant": 1500.00,
  "fournisseur": "Fournisseur ABC SARL",
  "nomFichierOriginal": "facture.pdf",
  "statut": "VALIDE",
  "commentaireComptable": "Document validé après vérification",
  "dateValidation": "2024-01-20T14:30:00",
  "createdAt": "2024-01-20T10:30:00",
  "updatedAt": "2024-01-20T14:30:00",
  "societeId": 1,
  "societeRaisonSociale": "TechnoMaroc SARL"
}
```

**Note:** 
- Document status changes from `EN_ATTENTE` to `VALIDE`
- `dateValidation` is set to current timestamp
- `commentaire` is optional (can be null)

---

## 4. Reject Document

### **Endpoint:** `POST /api/comptable/documents/{id}/rejeter`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
Content-Type: application/json
```

### **Request Body (JSON) - Required:**
```json
{
  "commentaire": "Montant incorrect, facture non conforme"
}
```

**⚠️ Important:** `commentaire` is **MANDATORY** for rejection!

### **URL Example:**
```
POST http://localhost:8080/api/comptable/documents/2/rejeter
```

### **Response Example:**
```json
{
  "id": 2,
  "numeroPiece": "FAC-2024-002",
  "type": "FACTURE_VENTE",
  "categorieComptable": "Ventes de produits",
  "datePiece": "2024-01-16",
  "montant": 2500.00,
  "fournisseur": "Client XYZ",
  "nomFichierOriginal": "vente.pdf",
  "statut": "REJETE",
  "commentaireComptable": "Montant incorrect, facture non conforme",
  "dateValidation": "2024-01-20T15:00:00",
  "createdAt": "2024-01-17T09:15:00",
  "updatedAt": "2024-01-20T15:00:00",
  "societeId": 2,
  "societeRaisonSociale": "Atlas Import Export"
}
```

**Note:** 
- Document status changes from `EN_ATTENTE` to `REJETE`
- `commentaire` (motif) is **required** - request will fail with 400 if missing
- `dateValidation` is set to current timestamp

### **Error Example (Missing Commentaire):**
```json
{
  "timestamp": "2024-01-20T15:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Le motif de rejet est obligatoire",
  "path": "/api/comptable/documents/2/rejeter"
}
```

---

## 5. Get Documents by Société

### **Endpoint:** `GET /api/comptable/documents/societe/{societeId}`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Request Body:**
**None** (GET request)

### **URL Example:**
```
GET http://localhost:8080/api/comptable/documents/societe/1
```

### **Response Example:**
```json
[
  {
    "id": 1,
    "numeroPiece": "FAC-2024-001",
    "type": "FACTURE_ACHAT",
    "categorieComptable": "Achats de marchandises",
    "datePiece": "2024-01-15",
    "montant": 1500.00,
    "fournisseur": "Fournisseur ABC SARL",
    "nomFichierOriginal": "facture.pdf",
    "statut": "VALIDE",
    "commentaireComptable": "Document validé",
    "dateValidation": "2024-01-20T14:30:00",
    "createdAt": "2024-01-20T10:30:00",
    "updatedAt": "2024-01-20T14:30:00",
    "societeId": 1,
    "societeRaisonSociale": "TechnoMaroc SARL"
  },
  {
    "id": 3,
    "numeroPiece": "FAC-2024-003",
    "type": "TICKET_CAISSE",
    "categorieComptable": "Frais généraux",
    "datePiece": "2024-01-18",
    "montant": 45.50,
    "fournisseur": "Supermarché ABC",
    "nomFichierOriginal": "ticket.pdf",
    "statut": "EN_ATTENTE",
    "commentaireComptable": null,
    "dateValidation": null,
    "createdAt": "2024-01-19T11:00:00",
    "updatedAt": null,
    "societeId": 1,
    "societeRaisonSociale": "TechnoMaroc SARL"
  }
]
```

**Note:** Returns all documents (all statuses) for the specified société.

---

## 6. Get Documents by Exercice

### **Endpoint:** `GET /api/comptable/documents/exercice/{exercice}`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Request Body:**
**None** (GET request)

### **URL Example:**
```
GET http://localhost:8080/api/comptable/documents/exercice/2024
```

### **Response Example:**
```json
[
  {
    "id": 1,
    "numeroPiece": "FAC-2024-001",
    "type": "FACTURE_ACHAT",
    "categorieComptable": "Achats de marchandises",
    "datePiece": "2024-01-15",
    "montant": 1500.00,
    "fournisseur": "Fournisseur ABC SARL",
    "nomFichierOriginal": "facture.pdf",
    "statut": "VALIDE",
    "commentaireComptable": "Document validé",
    "dateValidation": "2024-01-20T14:30:00",
    "createdAt": "2024-01-20T10:30:00",
    "updatedAt": "2024-01-20T14:30:00",
    "societeId": 1,
    "societeRaisonSociale": "TechnoMaroc SARL"
  },
  {
    "id": 2,
    "numeroPiece": "FAC-2024-002",
    "type": "FACTURE_VENTE",
    "categorieComptable": "Ventes de produits",
    "datePiece": "2024-01-16",
    "montant": 2500.00,
    "fournisseur": "Client XYZ",
    "nomFichierOriginal": "vente.pdf",
    "statut": "REJETE",
    "commentaireComptable": "Montant incorrect",
    "dateValidation": "2024-01-20T15:00:00",
    "createdAt": "2024-01-17T09:15:00",
    "updatedAt": "2024-01-20T15:00:00",
    "societeId": 2,
    "societeRaisonSociale": "Atlas Import Export"
  }
]
```

**Note:** Returns all documents (all statuses, all sociétés) for the specified exercice comptable.

---

## 📝 Complete Testing Workflow

### **Step 1: Login as Comptable**
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "mohamed@comptable.com",
  "password": "123456789"
}
```
**Save the token!**

### **Step 2: Get Pending Documents**
```bash
GET http://localhost:8080/api/comptable/documents/en-attente
Authorization: Bearer {TOKEN}
```
**Note the document IDs that need validation/rejection.**

### **Step 3: Validate a Document**
```bash
POST http://localhost:8080/api/comptable/documents/1/valider
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "commentaire": "Document conforme, validé"
}
```

### **Step 4: Reject a Document**
```bash
POST http://localhost:8080/api/comptable/documents/2/rejeter
Authorization: Bearer {TOKEN}
Content-Type: application/json

{
  "commentaire": "Facture non conforme, montant erroné"
}
```

### **Step 5: View Documents by Société**
```bash
GET http://localhost:8080/api/comptable/documents/societe/1
Authorization: Bearer {TOKEN}
```

### **Step 6: View Documents by Exercice**
```bash
GET http://localhost:8080/api/comptable/documents/exercice/2024
Authorization: Bearer {TOKEN}
```

---

## ⚠️ Common Errors & Solutions

### **Error 403: Forbidden**
- **Cause:** User is not a COMPTABLE
- **Solution:** Login with a comptable account (e.g., `mohamed@comptable.com`)

### **Error 400: Bad Request (Reject)**
- **Cause:** Missing `commentaire` when rejecting a document
- **Solution:** Always provide `commentaire` in the request body when rejecting

### **Error 404: Not Found**
- **Cause:** Document ID doesn't exist
- **Solution:** Use a valid document ID

### **Error 400: Bad Request (Validate/Reject)**
- **Cause:** Document is not in `EN_ATTENTE` status
- **Solution:** Only documents with status `EN_ATTENTE` can be validated or rejected

---

## 🔐 Security Notes

- ✅ All endpoints require `COMPTABLE` role
- ✅ Only comptables can validate/reject documents
- ✅ Comptables can see documents from all sociétés
- ✅ Validation commentaire is optional
- ✅ Rejection commentaire (motif) is mandatory

---

## ✅ Quick Test Checklist

- [ ] Login as COMPTABLE user
- [ ] Get pending documents (should see all EN_ATTENTE documents)
- [ ] Validate a document (with optional commentaire)
- [ ] Validate a document (without commentaire - should work)
- [ ] Reject a document (with mandatory commentaire)
- [ ] Try to reject without commentaire (should fail with 400)
- [ ] Get documents by société ID
- [ ] Get documents by exercice comptable
- [ ] Try to access as SOCIETE user (should fail with 403)

---

**Happy Testing! 🚀**
