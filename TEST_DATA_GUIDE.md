# 🧪 Test Users & Data Seeding Guide

## ✅ Database Seeding Complete!

Your application now automatically seeds the database with test data on startup.

---

## 📊 Test Data Summary

### **5 Sociétés (Companies)**

| Raison Sociale | ICE | Adresse | Téléphone | Email |
|----------------|-----|---------|-----------|-------|
| TechnoMaroc SARL | 002567891234567 | Zone Industrielle Sidi Moumen, Casablanca | +212522345678 | contact@technomaroc.ma |
| Atlas Import Export | 002789123456789 | Boulevard Mohamed V, Rabat | +212537654321 | info@atlasimportexport.ma |
| Maghreb Services | 002891234567890 | Avenue Hassan II, Marrakech | +212524987654 | contact@maghrebservices.ma |
| Digital Solutions Maroc | 002912345678901 | Quartier des Affaires, Tanger | +212539876543 | hello@digitalsolutions.ma |
| Sahara Trading | 003012345678912 | Zone Franche, Agadir | +212528765432 | contact@saharatrading.ma |

### **3 Comptables (Accountants)**

| Email | Password | Nom Complet | Société |
|-------|----------|-------------|---------|
| mohamed@comptable.com | 123456789 | Mohamed Alami | None (Independent) |
| fatima@comptable.com | 123456789 | Fatima Benani | None (Independent) |
| youssef@comptable.com | 123456789 | Youssef Tazi | None (Independent) |

### **6 Société Users (Company Representatives)**

| Email | Password | Nom Complet | Société |
|-------|----------|-------------|---------|
| mohamed@societe.com | 123456789 | Mohamed Benjelloun | TechnoMaroc SARL |
| ahmed@societe.com | 123456789 | Ahmed Tazi | Atlas Import Export |
| sara@societe.com | 123456789 | Sara Alaoui | Maghreb Services |
| karim@societe.com | 123456789 | Karim El Fassi | Digital Solutions Maroc |
| leila@societe.com | 123456789 | Leila Chraibi | Sahara Trading |
| omar@societe.com | 123456789 | Omar Benkirane | TechnoMaroc SARL |

---

## 🧪 Testing Authentication

### **Test 1: Login as Comptable**

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "mohamed@comptable.com",
    "password": "123456789"
  }'
```

**Expected Response:**
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

**Note:** Comptable users don't have `societeId` or `societeRaisonSociale` because they work for the cabinet, not for a specific company.

---

### **Test 2: Login as Société User**

**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "mohamed@societe.com",
    "password": "123456789"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 6,
  "email": "mohamed@societe.com",
  "nomComplet": "Mohamed Benjelloun",
  "role": "SOCIETE",
  "societeId": 1,
  "societeRaisonSociale": "TechnoMaroc SARL"
}
```

**Note:** Société users have `societeId` and `societeRaisonSociale` because they represent a specific company.

---

### **Test 3: Login with Different Société Users**

All société users have the same password: `123456789`

```bash
# Login as Ahmed (Atlas Import Export)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "ahmed@societe.com", "password": "123456789"}'

# Login as Sara (Maghreb Services)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "sara@societe.com", "password": "123456789"}'

# Login as Karim (Digital Solutions Maroc)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "karim@societe.com", "password": "123456789"}'
```

---

## 🔍 Viewing Data in H2 Console

### **Access H2 Console:**
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:cabinet_comptable
Username: root
Password: root
```

### **Query Examples:**

**1. View all Sociétés:**
```sql
SELECT * FROM societes;
```

**2. View all Users:**
```sql
SELECT * FROM utilisateurs;
```

**3. View Comptables only:**
```sql
SELECT * FROM utilisateurs WHERE role = 'COMPTABLE';
```

**4. View Société Users only:**
```sql
SELECT * FROM utilisateurs WHERE role = 'SOCIETE';
```

**5. View Users with their Société:**
```sql
SELECT u.email, u.nom_complet, u.role, s.raison_sociale 
FROM utilisateurs u 
LEFT JOIN societes s ON u.societe_id = s.id;
```

**6. Count users per société:**
```sql
SELECT s.raison_sociale, COUNT(u.id) as user_count
FROM societes s
LEFT JOIN utilisateurs u ON s.id = u.societe_id
GROUP BY s.raison_sociale;
```

---

## 📝 Postman Collection

### **Create a Postman Collection with these requests:**

#### **1. Login - Comptable**
```
Method: POST
URL: http://localhost:8080/api/auth/login
Headers: Content-Type: application/json
Body (raw JSON):
{
  "email": "mohamed@comptable.com",
  "password": "123456789"
}
```

#### **2. Login - Société (TechnoMaroc)**
```
Method: POST
URL: http://localhost:8080/api/auth/login
Headers: Content-Type: application/json
Body (raw JSON):
{
  "email": "mohamed@societe.com",
  "password": "123456789"
}
```

#### **3. Login - Société (Atlas Import)**
```
Method: POST
URL: http://localhost:8080/api/auth/login
Headers: Content-Type: application/json
Body (raw JSON):
{
  "email": "ahmed@societe.com",
  "password": "123456789"
}
```

---

## 🎯 Test Scenarios

### **Scenario 1: Multiple Users from Same Company**

**Context:** TechnoMaroc SARL has 2 users:
- mohamed@societe.com (Mohamed Benjelloun)
- omar@societe.com (Omar Benkirane)

**Test:**
1. Login as `mohamed@societe.com`
2. Upload a document
3. Login as `omar@societe.com`
4. Both should see the same documents (they belong to the same société)

---

### **Scenario 2: Comptable Can See All Documents**

**Test:**
1. Login as `mohamed@societe.com` (TechnoMaroc)
2. Upload documents
3. Login as `ahmed@societe.com` (Atlas Import)
4. Upload documents
5. Login as `mohamed@comptable.com`
6. Should see documents from BOTH companies

---

### **Scenario 3: Société Can Only See Own Documents**

**Test:**
1. Login as `mohamed@societe.com` (TechnoMaroc)
2. Upload documents
3. Login as `ahmed@societe.com` (Atlas Import)
4. Should NOT see TechnoMaroc's documents
5. Should only see Atlas Import documents

---

## 🔐 Security Testing

### **Test Invalid Credentials:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "mohamed@comptable.com",
    "password": "wrongpassword"
  }'
```

**Expected:** 401 Unauthorized or "Email ou mot de passe incorrect"

---

### **Test Non-Existent User:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "notexist@test.com",
    "password": "123456789"
  }'
```

**Expected:** 401 Unauthorized or "Email ou mot de passe incorrect"

---

## 🚀 How DataSeeder Works

### **Implementation Details:**

**File:** `src/main/java/.../config/DataSeeder.java`

**Key Points:**
1. ✅ Implements `CommandLineRunner` - runs on application startup
2. ✅ Checks if data exists - only seeds if database is empty
3. ✅ Uses `PasswordEncoder` - passwords are BCrypt encrypted
4. ✅ Creates relationships - Société users are linked to their companies
5. ✅ Sets timestamps - `createdAt` for audit trail

**Seeding Order:**
```
1. Create Sociétés (5 companies)
2. Create Comptables (3 accountants)
3. Create Société Users (6 company representatives)
4. Display summary in console
```

---

## 📋 Data Model Relationships

```
┌─────────────────┐         ┌─────────────────┐
│    Societe      │         │      User       │
├─────────────────┤         ├─────────────────┤
│ id              │◄───────┤│ societe_id      │
│ raison_sociale  │  Many  ││ email           │
│ ice             │   to   ││ password        │
│ adresse         │  One   ││ nom_complet     │
│ telephone       │         │ role            │
│ email_contact   │         │ actif           │
└─────────────────┘         └─────────────────┘
```

**Relationship Rules:**
- ✅ One Société can have multiple Users
- ✅ SOCIETE role users MUST have a société
- ✅ COMPTABLE role users have NO société (null)

---

## 🎓 Understanding the Data

### **Why 5 Sociétés?**
Your context mentions: "Le Cabinet Comptable Al Amane gère actuellement 35 sociétés"
- 5 societies provide enough variety for testing
- Easy to expand by copying the pattern

### **Why 3 Comptables?**
- Represents the accounting team at Cabinet Al Amane
- Can test workload distribution
- Can test different accountant validating different documents

### **Why 6 Société Users?**
- Tests multiple users per company (TechnoMaroc has 2)
- Tests one user per company (others have 1 each)
- Covers real-world scenario where companies have multiple representatives

---

## 🔄 Re-Seeding Database

### **Method 1: Restart Application**
```bash
# Stop application (Ctrl+C)
# Start again
mvn spring-boot:run
```
Database is re-created (H2 in-memory) and re-seeded automatically.

### **Method 2: Manual Re-seed via H2 Console**
```sql
-- Delete all data
DELETE FROM documents;
DELETE FROM utilisateurs;
DELETE FROM societes;
```
Then restart application.

---

## 🎉 Success Confirmation

When application starts, you should see:
```
🌱 Seeding database with test data...
✅ Created 5 societies
✅ Created 3 comptables
✅ Created 6 société users

╔══════════════════════════════════════════════════════════════════╗
║                    🎉 Database Seeded Successfully! 🎉           ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  📊 Test Data Summary:                                          ║
║  • 5 Sociétés                                                   ║
║  • 3 Comptables (Accountants)                                   ║
║  • 6 Société Users (Company representatives)                    ║
...
```

---

## 📖 Next Steps

Now that you have test data:

1. ✅ **Test Authentication** - Login with different users
2. ✅ **Implement Document Controller** - Upload, validate, reject
3. ✅ **Test Role-Based Access** - Verify société users can't see other companies' docs
4. ✅ **Test Comptable Features** - Validate/reject documents from all companies
5. ✅ **Add More Test Data** - Expand seeder with documents if needed

---

## 🔍 Quick Reference

### **Comptable Login (for testing validation/rejection):**
```
Email: mohamed@comptable.com
Password: 123456789
```

### **Société Login (for testing document upload):**
```
Email: mohamed@societe.com
Password: 123456789
Company: TechnoMaroc SARL
```

### **H2 Console Access:**
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:cabinet_comptable
Username: root
Password: root
```

---

**Your database is now fully seeded and ready for testing! 🚀**

