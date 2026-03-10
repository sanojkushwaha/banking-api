# 🏦 Banking & Finance REST API

A production-ready RESTful Banking API built with **Spring Boot 3**, **Spring Security**, and **JWT Authentication**.

---

## 📋 Table of Contents
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [How to Run (IntelliJ IDEA)](#how-to-run-intellij-idea)
- [API Endpoints](#api-endpoints)
- [Testing with Swagger UI](#testing-with-swagger-ui)
- [Switch to MySQL](#switch-to-mysql)
- [GitHub Deployment](#github-deployment)
- [Tech Stack](#tech-stack)

---

## ✅ Prerequisites

Before running the project, make sure you have installed:

| Tool | Version | Download |
|------|---------|----------|
| **Java JDK** | 17 or higher | https://adoptium.net |
| **Maven** | 3.8+ | https://maven.apache.org (or use IntelliJ's bundled Maven) |
| **IntelliJ IDEA** | Community or Ultimate | https://www.jetbrains.com/idea |
| **Git** | Latest | https://git-scm.com |

> ⚠️ MySQL is **NOT required**. The project uses **H2 in-memory database** by default — works out of the box!

### IntelliJ IDEA Setup
1. Open IntelliJ → `File` → `Settings` → `Plugins` → Install **Lombok Plugin**
2. Go to `Settings` → `Build` → `Compiler` → `Annotation Processors` → ✅ Enable annotation processing
3. Make sure your **Project SDK** is set to Java 17: `File` → `Project Structure` → `SDK`

---

## 📁 Project Structure

```
banking-api/
├── pom.xml                          ← Maven dependencies
└── src/
    ├── main/
    │   ├── java/com/banking/
    │   │   ├── BankingApiApplication.java     ← App entry point
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java        ← Spring Security setup
    │   │   │   └── SwaggerConfig.java         ← Swagger/OpenAPI setup
    │   │   ├── controller/
    │   │   │   ├── AuthController.java        ← Register & Login
    │   │   │   └── AccountController.java     ← Account & Transactions
    │   │   ├── dto/
    │   │   │   ├── request/
    │   │   │   │   ├── RegisterRequest.java
    │   │   │   │   ├── LoginRequest.java
    │   │   │   │   └── TransactionRequest.java
    │   │   │   └── response/
    │   │   │       ├── ApiResponse.java        ← Generic API wrapper
    │   │   │       ├── AuthResponse.java
    │   │   │       ├── AccountResponse.java
    │   │   │       └── TransactionResponse.java
    │   │   ├── entity/
    │   │   │   ├── User.java                  ← User table
    │   │   │   ├── Account.java               ← Account table
    │   │   │   └── Transaction.java           ← Transaction table
    │   │   ├── enums/
    │   │   │   ├── Role.java                  ← ROLE_USER, ROLE_ADMIN
    │   │   │   └── TransactionType.java       ← DEPOSIT, WITHDRAWAL, TRANSFER
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java ← Centralized error handling
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   ├── InsufficientFundsException.java
    │   │   │   └── DuplicateResourceException.java
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java
    │   │   │   ├── AccountRepository.java
    │   │   │   └── TransactionRepository.java
    │   │   ├── security/
    │   │   │   ├── filter/
    │   │   │   │   └── JwtAuthenticationFilter.java ← Validates JWT on each request
    │   │   │   └── service/
    │   │   │       └── JwtService.java              ← Generate & validate tokens
    │   │   └── service/
    │   │       ├── AuthService.java           ← Interface
    │   │       ├── AccountService.java        ← Interface
    │   │       └── impl/
    │   │           ├── AuthServiceImpl.java   ← Auth business logic
    │   │           ├── AccountServiceImpl.java ← Banking business logic
    │   │           └── UserDetailsServiceImpl.java ← Spring Security user loader
    │   └── resources/
    │       └── application.properties        ← App configuration
    └── test/
        ├── java/com/banking/
        │   └── BankingApiApplicationTests.java
        └── resources/
            └── application-test.properties
```

---

## 🚀 How to Run (IntelliJ IDEA)

### Step 1 — Open the Project
```
File → Open → Select the 'banking-api' folder → Click OK
```

### Step 2 — Wait for Maven to Download Dependencies
- IntelliJ will auto-detect `pom.xml` and download all dependencies
- Watch the progress bar at the bottom right
- This takes 1-3 minutes on first run

### Step 3 — Run the Application
- Open `BankingApiApplication.java`
- Click the ▶️ green **Run** button next to the `main` method
- Or press `Shift + F10`

### Step 4 — Verify it Started
Look for this in the console:
```
╔══════════════════════════════════════════════════╗
║       Banking API Started Successfully!          ║
║  Swagger UI  : http://localhost:8080/swagger-ui.html  ║
║  H2 Console  : http://localhost:8080/h2-console       ║
╚══════════════════════════════════════════════════╝
```

### Step 5 — Open Swagger UI
Go to: **http://localhost:8080/swagger-ui.html**

---

## 📌 API Endpoints

### 🔓 Public (No token required)
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT |

### 🔐 Protected (JWT required)
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/accounts/me` | Get my account details |
| POST | `/api/accounts/deposit` | Deposit money |
| POST | `/api/accounts/withdraw` | Withdraw money |
| POST | `/api/accounts/transfer` | Transfer to another account |
| GET | `/api/accounts/transactions` | Transaction history (paginated) |
| GET | `/api/accounts/{accountNumber}` | Get account by number **(ADMIN only)** |

---

## 🧪 Testing with Swagger UI

### 1. Register a user
```json
POST /api/auth/register
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phoneNumber": "9876543210"
}
```

### 2. Login to get token
```json
POST /api/auth/login
{
  "email": "john@example.com",
  "password": "password123"
}
```
Copy the `token` value from the response.

### 3. Authorize in Swagger
- Click **Authorize** button (top right of Swagger UI)
- Enter: `Bearer <paste-your-token-here>`
- Click Authorize

### 4. Deposit money
```json
POST /api/accounts/deposit
{
  "amount": 5000.00,
  "description": "Initial deposit"
}
```

### 5. Transfer money
```json
POST /api/accounts/transfer
{
  "amount": 1000.00,
  "targetAccountNumber": "1234567890",
  "description": "Payment"
}
```

### 6. View transaction history
```
GET /api/accounts/transactions?page=0&size=10
```

---

## 🗄️ Switch to MySQL (Production)

1. Create a MySQL database:
```sql
CREATE DATABASE banking_db;
```

2. In `application.properties`, comment out the H2 section and uncomment MySQL:
```properties
# Comment these H2 lines:
# spring.datasource.url=jdbc:h2:mem:bankingdb...

# Uncomment these MySQL lines:
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

----

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Programming language |
| Spring Boot 3.2 | Application framework |
| Spring Security | Authentication & Authorization |
| JWT (jjwt 0.11.5) | Stateless token-based auth |
| Spring Data JPA | ORM / Database access |
| H2 Database | In-memory DB for development |
| MySQL | Production database |
| Lombok | Reduce boilerplate code |
| Swagger/OpenAPI | API documentation |
| Maven | Build tool & dependency management |

---

## 👤 Author and Developed by
**Sanoj Kumar Kushwaha**  
4th Year Computer Science Student  
GitHub: https://github.com/Sanoj Kushwaha

---

## 📄 License
This project is open source and available under the [MIT License](LICENSE).
