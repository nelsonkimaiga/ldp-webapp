# Client Backend (Demo App with IdP Integration)

This is a sample backend application demonstrating how to integrate a **legacy system** with the centralized **Identity Provider (IdP)**.  
It replaces local authentication with JWT-based authentication and token validation using the IdP’s public key.

---

## Tech Stack
- **Java 17**
- **Spring Boot** (REST API)
- **Spring Security** (JWT validation)
- **Maven** (build tool)
- **Docker** (containerization)
- **H2** (in-memory DB for demo)

---

## Features
- Legacy login replaced with IdP authentication flow
- Validates access tokens issued by the IdP using the **public key**
- Demonstrates **role-based access**:
  - `ROLE_ADMIN` → can view all data
  - `ROLE_USER` → can only view their own data
- Refresh token flow handled via IdP

---

## Setup & Run

### 1. Clone the repo
```bash
git clone https://github.com/nelsonkimaiga/client-webapp-backend.git
cd client-webapp-backend
Update application.properties with your IdP base URL and JWKS endpoint:
Run with Maven: ./mvnw spring-boot:run
```
## Authentication Flow

User registers/logs in via IdP (Email+Password or LinkedIn).

IdP issues:

Access Token (short-lived)

Refresh Token (long-lived)

Client backend receives access token via frontend and attaches it to requests:

Authorization: Bearer <access_token>

## How to Test

Spin up the IdP (see IdP repo) -> https://github.com/nelsonkimaiga/ia-standalone-authentication

Obtain a valid access token via login/registration.

Call client backend endpoints with Authorization: Bearer <token>.

Verify role-based restrictions:

Deployment

Containerized with Docker for portability

Deployed to the cloud on DigitalOcean

Runs on port 8092
