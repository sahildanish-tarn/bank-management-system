# Bank Management System

A RESTful backend for a bank management system built with **Spring Boot**, supporting customer onboarding, account management, deposits/withdrawals/transfers, loan applications, and loan repayments — secured with JWT-based authentication.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA** (Hibernate)
- **Spring Security** with JWT authentication
- **MySQL / PostgreSQL** (relational DB)
- **Lombok**
- **Jackson** (`ObjectMapper` for entity ↔ DTO mapping)
- **AOP** (`@Aspect`) for request/response logging
- **Maven**

## Features

- **User Authentication** — signup, signin, JWT-based session (stateless), password update, role-based access (`CUSTOMER`, `ADMIN`)
- **Customer Management** — create, update, soft-delete customer profiles linked to a user account
- **Account Management** — open savings/current accounts, check balance, update status (active/blocked/closed), close accounts
- **Transactions** — deposit, withdraw, transfer between accounts, with full transaction history per account
- **Loans** — apply, approve/reject, track outstanding amount, close on full repayment
- **Loan Payments** — make installment payments against an active loan, view payment history
- **Centralized Exception Handling** — consistent JSON error responses across all custom exceptions
- **Request Logging** — method-level entry/exit/timing logs via Spring AOP

## Project Structure

```
com.jsp.bank_management_system
├── controller       # REST controllers
├── service           # Service interfaces
├── service.imp        # Service implementations
├── repository        # Spring Data JPA repositories
├── entity            # JPA entities
├── dto               # Request/response DTOs
├── enums             # Enum types (AccountStatus, LoanStatus, Role, etc.)
├── exception         # Custom exceptions + global exception handler
├── security          # JWT service, filter, UserDetails implementation
├── config            # Bean configuration (PasswordEncoder, etc.)
├── aop               # Logging aspect
└── util              # Shared response structures
```

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.8+
- MySQL (or your configured DB)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/sahildanish-tarn/bank-management-system.git
   cd bank-management-system
   ```

2. Configure your database in `src/main/resources/application.yml` (or `.properties`):
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/bank_management_db
       username: your_username
       password: your_password
     jpa:
       hibernate:
         ddl-auto: update
   ```

3. Set your JWT secret as an environment variable rather than hardcoding it:
   ```yaml
   jwt:
     secret: ${JWT_SECRET}
     expiration: 1800000
   ```

4. Build and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

The application starts on `http://localhost:8080` by default.

## API Overview

| Module | Base Path | Key Endpoints |
|---|---|---|
| User | `/api/v1/user` | `POST /signup`, `POST /signin`, `PUT /`, `PATCH /` (password) |
| Customer | `/api/v1/customers` | `POST /create`, `GET /{id}`, `GET /all`, `PUT /{id}`, `DELETE /{id}` |
| Account | `/api/v1/account` | `POST /`, `GET /{id}`, `GET /customer/{customerId}`, `PUT /{accountNumber}/status`, `DELETE /close/{accountNumber}` |
| Transaction | `/api/v1/transactions` | `POST /deposit`, `POST /withdraw`, `POST /transfer`, `GET /account/{accountNumber}` |
| Loan | `/api/v1/loan` | `POST /`, `PATCH /{loanNumber}/approve`, `PATCH /{loanNumber}/reject`, `PATCH /{loanNumber}/close` |
| Loan Payment | `/api/v1/loan-payment` | `POST /`, `GET /loan/{loanNumber}` |

`/signup` and `/signin` are public; all other endpoints require a valid JWT in the `Authorization: Bearer <token>` header.

## Authentication Flow

1. `POST /api/v1/user/signup` — create a user account
2. `POST /api/v1/user/signin` — returns a JWT
3. Include the token in the `Authorization` header for all subsequent requests

## Roadmap / Planned Improvements

- [ ] Unit tests (JUnit + Mockito) for the service layer
- [ ] Swagger/OpenAPI documentation
- [ ] Pagination on list endpoints
- [ ] Role-based endpoint authorization (`@PreAuthorize`)
- [ ] CI pipeline (GitHub Actions)

## Author

**S.M.Danish** — Java Full Stack Development Intern

