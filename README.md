# ACCOUNT MANAGEMENT KATA
## Overview
This project is a kata designed to showcase an Account Management system implemented with Spring Boot. It focuses on solving a specific problem through clean code and best practices. The key features include:
- Debit Operation: Deducts an amount from an account balance.
- Credit Operation: Adds an amount to an account balance.
- View Operations: Retrieves a paginated list of account operations.
- The kata demonstrates RESTful principles, proper architecture, and best practices for validation, exception handling, and testing.

## key features
- RESTful API Design: Endpoints for debit, credit, and fetching operations.
- Validation: Input data validated using Bean Validation annotations.
- Error Handling: Centralized exception handling using @ControllerAdvice.
- Persistence: Data stored in an in-memory H2 database, with schema management by Flyway.
- Testing: Comprehensive unit and integration tests.

## Technology Stack
- Java 17: Language.
- Spring Boot 3.x: Framework for rapid development.
- H2 Database: Lightweight, in-memory database.
- Flyway: Handles database migrations.
- JUnit 5: Testing framework.
- MockMvc: Testing REST controllers.
- Lombok: Reduces boilerplate code.
- MapStruct: Simplifies mapping between entities and DTOs.

## Setup Instructions
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

## Endpoints
| HTTP Method |             Endpoint             |       Description        |
|:-----------:|:--------------------------------:|:------------------------:|
|    `POST`     |   /api/v1/accounts/{id}/debit    |     Debit an account     |
|    `POST`     |   /api/v1/accounts/{id}/credit   |    Credit an account     |
|     `GET`     | /api/v1/accounts/{id}/operations | Fetch account operations |

## Validation rules
- Amount must not be null or negative: Proper error messages are returned for invalid inputs.
- Account existence:Operations fail gracefully if the account is not found.
- Insufficient Balance: Credit operation fail if the account balance not sufficient.

## Error Handling
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

