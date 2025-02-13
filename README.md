# LOAN API
## Overview
This project is a Loan API built with Spring Boot. It provides functionalities to create, list, and pay loans.

# Technologies Used
* Java 20.0.2 (Spring Boot)
* H2 Database
* Spring Security
* RESTful API

# Setup Instructions

1. Clone the repository
---
>git clone https://github.com/bilgeesahin/loan-api
---
2. Navigate to the project directory
---
>cd loan-api
---
3. Run the application
---
>mvn spring-boot:run
---

# Database Information
* ⁠The project uses H2 Database as an in-memory database
* Role types have been manually added in the database (you can find them in the "loandb.mv.db" file along with sample data)
* The customer role is automatically assigned to all users, while the admin role needs to be manually edited in the database

# API Endpoints
## Authentication
* Register User
  * >POST /api/loans/create
  * Body:
    {
    "username": "string",
    "password": "string",
    "name": "string",
    "surname": "string",
    "creditLimit": "number",
    "usedCreditLimit": "number"
    }
* Login User
  * >POST /auth/login
  * Body:
    {
    "username": "string",
    "password": "string"
    }
## Loan Management
* Create Loan
  * >POST /api/loans/create
  * Body:
  {
  "customerId": "string",
  "amount": "number",
  "interestRate": "number",
  "numberOfInstallments": "number"
  }
* List Loans
  * >GET /api/loans/{customerId}
* List Loan Installments
  * >GET /api/loans/{loanId}/installments
* Pay Loan
  * >PUT /api/loans/{loanId}/payment
  * Body:
  {
  "amountPaid": "number"
  }
