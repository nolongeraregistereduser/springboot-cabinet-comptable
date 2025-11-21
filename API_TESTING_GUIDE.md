# 🧪 API Testing Guide - Exact Request Bodies

This guide provides **exact JSON bodies and request formats** for testing all DocumentController endpoints.

---

## 📋 Table of Contents

1. [Authentication (Login)](#1-authentication-login)
2. [Upload Document](#2-upload-document)
3. [List Documents](#3-list-documents)
4. [Get Document by ID](#4-get-document-by-id)
5. [Download Document](#5-download-document)
6. [Delete Document](#6-delete-document)
7. [List by Exercice](#7-list-by-exercice)
8. [List by Société (Comptable Only)](#8-list-by-société-comptable-only)

---

## 1. Authentication (Login)

### **Endpoint:** `POST /api/auth/login`

### **Headers:**
```
Content-Type: application/json
```

### **Request Body (JSON):**
```json
{
  "email": "mohamed@societe.com",
  "password": "123456789"
}
```

### **Alternative - Login as Comptable:**
```json
{
  "email": "mohamed@comptable.com",
  "password": "123456789"
}
```

### **Response Example:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2hhbWVkQHNvY2lldGUuY29tIiwiaWF0IjoxNzAw...",
  "type": "Bearer",
  "userId": 4,
  "email": "mohamed@societe.com",
  "nomComplet": "Mohamed Benjelloun",
  "role": "SOCIETE",
  "societeId": 1,
  "societeRaisonSociale": "TechnoMaroc SARL"
}
```

**Save the `token` value for subsequent requests!**

---

## 2. Upload Document

### **Endpoint:** `POST /api/documents`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
Content-Type: multipart/form-data
```

### **Request Body (Multipart Form Data):**

**⚠️ Important:** This endpoint uses `multipart/form-data`, not JSON. You need to send form fields + file.

#### **Using Postman:**
1. Select **POST** method
2. Go to **Body** tab
3. Select **form-data** (NOT raw JSON)
4. Add these fields:

| Key | Type | Value |
|-----|------|-------|
| `numeroPiece` | Text | `FAC-2024-001` |
| `type` | Text | `FACTURE_ACHAT` |
| `categorieComptable` | Text | `Achats de marchandises` |
| `datePiece` | Text | `2024-01-15` |
| `montant` | Text | `1500.00` |
| `fournisseur` | Text | `Fournisseur ABC SARL` |
| `exerciceComptable` | Text | `2024` |
| `societeId` | Text | `1` |
| `fichier` | File | Select a PDF/JPG/PNG file (max 10MB) |

#### **Using cURL:**
```bash
curl -X POST http://localhost:8080/api/documents \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -F "numeroPiece=FAC-2024-001" \
  -F "type=FACTURE_ACHAT" \
  -F "categorieComptable=Achats de marchandises" \
  -F "datePiece=2024-01-15" \
  -F "montant=1500.00" \
  -F "fournisseur=Fournisseur ABC SARL" \
  -F "exerciceComptable=2024" \
  -F "societeId=1" \
  -F "fichier=@/path/to/your/file.pdf"
```

#### **Field Values Explained:**

| Field | Required | Valid Values | Example |
|-------|----------|--------------|----------|
| `numeroPiece` | ✅ Yes | String (max 50 chars), must be unique | `FAC-2024-001` |
| `type` | ✅ Yes | `FACTURE_ACHAT`, `FACTURE_VENTE`, `TICKET_CAISSE`, `RELEVE_BANCAIRE` | `FACTURE_ACHAT` |
| `categorieComptable` | ✅ Yes | String | `Achats de marchandises` |
| `datePiece` | ✅ Yes | Date format: `YYYY-MM-DD`, must be past or present | `2024-01-15` |
| `montant` | ✅ Yes | Decimal number (min 0.01) | `1500.00` |
| `fournisseur` | ✅ Yes | String | `Fournisseur ABC SARL` |
| `exerciceComptable` | ✅ Yes | Integer (2000-2100) | `2024` |
| `societeId` | ✅ Yes | Long (must match user's société) | `1` |
| `fichier` | ✅ Yes | File (PDF, JPG, PNG, max 10MB) | Select file |

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
  "statut": "EN_ATTENTE",
  "commentaireComptable": null,
  "dateValidation": null,
  "createdAt": "2024-01-20T10:30:00",
  "updatedAt": null,
  "societeId": 1,
  "societeRaisonSociale": "TechnoMaroc SARL"
}
```

---

## 3. List Documents

### **Endpoint:** `GET /api/documents`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Query Parameters (Optional):**
```
?exerciceComptable=2024
```

### **Request Body:**
**None** (GET request)

### **Full URL Examples:**

**List all documents:**
```
GET http://localhost:8080/api/documents
```

**List documents by exercice:**
```
GET http://localhost:8080/api/documents?exerciceComptable=2024
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
    "statut": "VALIDE",
    "commentaireComptable": "Document validé",
    "dateValidation": "2024-01-18T14:20:00",
    "createdAt": "2024-01-17T09:15:00",
    "updatedAt": "2024-01-18T14:20:00",
    "societeId": 1,
    "societeRaisonSociale": "TechnoMaroc SARL"
  }
]
```

**Note:** 
- **SOCIETE users** see only their société's documents
- **COMPTABLE users** see all documents from all sociétés

---

## 4. Get Document by ID

### **Endpoint:** `GET /api/documents/{id}`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Request Body:**
**None** (GET request)

### **URL Example:**
```
GET http://localhost:8080/api/documents/1
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
  "statut": "EN_ATTENTE",
  "commentaireComptable": null,
  "dateValidation": null,
  "createdAt": "2024-01-20T10:30:00",
  "updatedAt": null,
  "societeId": 1,
  "societeRaisonSociale": "TechnoMaroc SARL"
}
```

---

## 5. Download Document

### **Endpoint:** `GET /api/documents/{id}/download`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Request Body:**
**None** (GET request)

### **URL Example:**
```
GET http://localhost:8080/api/documents/1/download
```

### **Response:**
- **Content-Type:** `application/pdf` (or `image/jpeg`, `image/png`)
- **Content-Disposition:** `attachment; filename="facture.pdf"`
- **Body:** Binary file content

**Note:** The file will be downloaded automatically in browsers/Postman.

---

## 6. Delete Document

### **Endpoint:** `DELETE /api/documents/{id}`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Request Body:**
**None** (DELETE request)

### **URL Example:**
```
DELETE http://localhost:8080/api/documents/1
```

### **Response:**
- **Status Code:** `204 No Content`
- **Body:** Empty

---

## 7. List by Exercice

### **Endpoint:** `GET /api/documents/exercice/{exercice}`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

### **Request Body:**
**None** (GET request)

### **URL Example:**
```
GET http://localhost:8080/api/documents/exercice/2024
```

### **Response Example:**
Same format as "List Documents" endpoint, but filtered by exercice.

---

## 8. List by Société (Comptable Only)

### **Endpoint:** `GET /api/documents/societe/{societeId}`

### **Headers:**
```
Authorization: Bearer {YOUR_TOKEN_HERE}
```

**⚠️ Note:** Only **COMPTABLE** users can access this endpoint!

### **Request Body:**
**None** (GET request)

### **URL Example:**
```
GET http://localhost:8080/api/documents/societe/1
```

### **Response Example:**
Same format as "List Documents" endpoint, but filtered by société ID.

---

## 📝 Complete Testing Workflow

### **Step 1: Login as SOCIETE User**
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "mohamed@societe.com",
  "password": "123456789"
}
```

**Save the token from response!**

### **Step 2: Upload a Document**
```bash
POST http://localhost:8080/api/documents
Authorization: Bearer {TOKEN_FROM_STEP_1}
Content-Type: multipart/form-data

Form Data:
- numeroPiece: FAC-2024-001
- type: FACTURE_ACHAT
- categorieComptable: Achats de marchandises
- datePiece: 2024-01-15
- montant: 1500.00
- fournisseur: Fournisseur ABC SARL
- exerciceComptable: 2024
- societeId: 1
- fichier: [Select PDF/JPG/PNG file]
```

**Save the document ID from response!**

### **Step 3: List Documents**
```bash
GET http://localhost:8080/api/documents
Authorization: Bearer {TOKEN_FROM_STEP_1}
```

### **Step 4: Get Document by ID**
```bash
GET http://localhost:8080/api/documents/1
Authorization: Bearer {TOKEN_FROM_STEP_1}
```

### **Step 5: Download Document**
```bash
GET http://localhost:8080/api/documents/1/download
Authorization: Bearer {TOKEN_FROM_STEP_1}
```

### **Step 6: Delete Document (Optional)**
```bash
DELETE http://localhost:8080/api/documents/1
Authorization: Bearer {TOKEN_FROM_STEP_1}
```

---

## 🔐 Testing with Different Roles

### **Test as SOCIETE User:**
1. Login with: `mohamed@societe.com` / `123456789`
2. Upload document (must use `societeId: 1` - their société)
3. List documents (only sees their société's documents)
4. Cannot access other sociétés' documents

### **Test as COMPTABLE User:**
1. Login with: `mohamed@comptable.com` / `123456789`
2. List documents (sees ALL documents from all sociétés)
3. Can access any document
4. Can use `/api/documents/societe/{id}` endpoint

---

## ⚠️ Common Errors & Solutions

### **Error 401: Unauthorized**
- **Cause:** Missing or invalid token
- **Solution:** Login again and use the new token

### **Error 403: Forbidden**
- **Cause:** SOCIETE user trying to access another société's document
- **Solution:** Use your own société's documents only

### **Error 400: Bad Request (Upload)**
- **Cause:** Invalid field values or missing required fields
- **Solution:** Check all required fields are present and valid

### **Error 404: Not Found**
- **Cause:** Document ID doesn't exist
- **Solution:** Use a valid document ID

### **Error: File validation failed**
- **Cause:** File type not PDF/JPG/PNG or size > 10MB
- **Solution:** Use valid file type and ensure size < 10MB

---

## 🧪 Sample Test Data

### **Document Type 1: Facture d'Achat**
```
numeroPiece: FAC-2024-001
type: FACTURE_ACHAT
categorieComptable: Achats de marchandises
datePiece: 2024-01-15
montant: 1500.00
fournisseur: Fournisseur ABC SARL
exerciceComptable: 2024
```

### **Document Type 2: Facture de Vente**
```
numeroPiece: FV-2024-001
type: FACTURE_VENTE
categorieComptable: Ventes de produits
datePiece: 2024-01-16
montant: 2500.00
fournisseur: Client XYZ
exerciceComptable: 2024
```

### **Document Type 3: Ticket de Caisse**
```
numeroPiece: TC-2024-001
type: TICKET_CAISSE
categorieComptable: Frais généraux
datePiece: 2024-01-17
montant: 45.50
fournisseur: Supermarché ABC
exerciceComptable: 2024
```

### **Document Type 4: Relevé Bancaire**
```
numeroPiece: RB-2024-001
type: RELEVE_BANCAIRE
categorieComptable: Opérations bancaires
datePiece: 2024-01-18
montant: 10000.00
fournisseur: Banque Populaire
exerciceComptable: 2024
```

---

## 📱 Postman Collection Setup

### **Environment Variables:**
Create a Postman environment with:
- `base_url`: `http://localhost:8080`
- `token`: (will be set automatically after login)

### **Pre-request Script (for Login):**
```javascript
// After login, save token
pm.environment.set("token", pm.response.json().token);
```

### **Authorization (for all other requests):**
- Type: Bearer Token
- Token: `{{token}}`

---

## ✅ Quick Test Checklist

- [ ] Login as SOCIETE user
- [ ] Upload document with valid data
- [ ] List documents (should see only your société's)
- [ ] Get document by ID
- [ ] Download document file
- [ ] Try to access another société's document (should fail with 403)
- [ ] Login as COMPTABLE user
- [ ] List documents (should see all)
- [ ] Access any document (should work)
- [ ] Delete document

---

**Happy Testing! 🚀**
