# ACCOUNT MANAGEMENT KATA
## **Table of Contents**
1. [Overview](#overview)
2. [Terminology](#Terminology)
3. [key features](#key)
4. [Technology Stack](#Technology)
5. [Setup Instructions](#Setup-Instructions)
6. [Endpoints](#Endpoints)
7. [Validation rules](#Validation-rules)
8. [Error Handling](#Error-Handling)
9. [Logging](#Logging)
10. [Testing](#Testing)
11. [Database](#Database)
12. [Future Enhancements and Optimizations](#Future-Enhancements-and-Optimizations)
## **Overview**
This project is a kata designed to showcase an Account Management system implemented with Spring Boot. It focuses on solving a specific problem through clean code and best practices. The key features include:
- Withdraw an amount of money from an account.
- Save an amount of money to an account.
- View Operations: Retrieves a paginated list of account operations.
- The kata demonstrates RESTful principles, proper architecture, and best practices for validation, exception handling, and testing.

### **Terminology**
- Credit: refer to operation of withdraw money.
- Debit: refer to operation of save money (add money to account balance).

## **key** features
- RESTful API Design: Endpoints for debit, credit, and fetching operations.
- Validation: Input data validated using Bean Validation annotations.
- Error Handling: Centralized exception handling using @ControllerAdvice.
- Persistence: Data stored in an in-memory H2 database, with schema management by Flyway.
- Testing: Comprehensive unit and integration tests.

## **Technology** Stack
- Java 17: Language.
- Spring Boot 3.x: Framework for rapid development.
- H2 Database: Lightweight, in-memory database.
- Flyway: Handles database migrations.
- JUnit 5: Testing framework.
- MockMvc: Testing REST controllers.
- Lombok: Reduces boilerplate code.
- MapStruct: Simplifies mapping between entities and DTOs.

## **Setup Instructions**
### Prerequisites
- Install Java 17 or later.
- Install Maven 3.x.
### Running the Application
1- Clone the repository
   ```bash
   git clone https://github.com/Hamzaoui01/kata-bank-account
   cd kata
   ```
2- Build and run the application:
   ```bash
    mvn clean
    mvn spring-boot:run
   ```
3- Access the API Documentation
- Swagger-UI: http://localhost:8080/swagger-ui.html

## **Endpoints**
| HTTP Method |             Endpoint             |       Description        |
|:-----------:|:--------------------------------:|:------------------------:|
|    `POST`     |   /api/v1/accounts/{id}/debit    |     Debit an account     |
|    `POST`     |   /api/v1/accounts/{id}/credit   |    Credit an account     |
|     `GET`     | /api/v1/accounts/{id}/operations | Fetch account operations |

## **Validation rules**
- Amount must not be null or negative: Proper error messages are returned for invalid inputs.
- Account existence:Operations fail gracefully if the account is not found.
- Insufficient Balance: Credit operation fail if the account balance not sufficient.

## **Error Handling**
Centralized exception handling ensures clean and consistent error responses. Below is an example error response:
```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Account not found with id 1",
  "path": "/api/v1/accounts/1/credit",
  "timestamp": "2024-12-16T04:57:37.8699155"
}

```
## Logging
This application uses SLF4J with the @Slf4j annotation for logging, relying on Spring Boot's default Logback configuration. The following log levels are implemented:

- INFO for high-level operations (e.g., debit/credit).
- DEBUG for detailed debugging in non-production environments.
- ERROR for critical issues or exceptions.

## Testing
### Unit Tests: 
- Follow the TDD approach (Test Driven Design).
- Test service and controller logic independently.
- Mock dependencies for isolated testing.
- Example: 
  - Debit operation with valid inputs.
  - Debit operation fails for non-existent account.
  - Validation errors for negative or null amounts.

  
To run tests:
```bash
mvn test
```

## Database
The schema and initial data are managed using Flyway. On application startup:
- The schema is created (tables for Account and Operation).
- Test data is preloaded for easier validation.
- You can modify migrations in src/main/resources/db/migration.

## Future Enhancements and Optimizations

### Strengthen Domain-Driven Design (DDD) Principles
- Current State: The Account entity encapsulates the domain logic for credit and debit operations, aligning well with DDD principles.
- Enhancements:
  - Introduce value objects for Amount to improve the domain model further.
  - Implement domain events to decouple the operation creation from the account debit/credit logic (e.g., using an event-driven approach).
### Expand Testing Coverage
- Incorporate integration tests to validate the full flow from the controller to the database.

### Security Enhancements
- Use rate-limiting to protect against abuse of critical endpoints, such as repeated debit or credit operations.

### Improve Performance and Scalability
- Implement caching to reduce the load on the database for frequently accessed data.

