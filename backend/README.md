# Keycloak Spring Boot Backend

A demo Spring Boot backend application showcasing Keycloak authentication integration using **Hexagonal Architecture** and **Domain-Driven Design (DDD)** principles.

## Architecture

This project follows **Hexagonal Architecture (Ports & Adapters)** with **DDD tactical patterns**:

```
backend/
├── shared/                          # Shared Kernel
│   └── Value Objects, Exceptions
├── user-domain/                     # User Bounded Context
│   ├── domain/                      # Core Domain (Pure, no frameworks)
│   │   ├── model/                  # Aggregates, Entities, Value Objects
│   │   └── port/                   # Ports (Interfaces)
│   ├── application/                 # Application Layer
│   │   ├── command/                # Commands
│   │   ├── query/                  # Queries
│   │   └── service/                # Use Case Services
│   ├── adapter/                     # Adapters
│   │   ├── in/rest/                # REST API (Controllers, DTOs)
│   │   └── out/                    # Outbound Adapters
│   │       ├── persistence/        # JPA Repositories
│   │       └── keycloak/           # Keycloak ACL
│   └── config/                      # Configuration
└── host-application/                # Host Application
    ├── Main Application
    ├── Security Configuration
    └── Database Migrations (Flyway)
```

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Data JPA**: Database persistence
- **Spring Security**: OAuth2 Resource Server
- **Keycloak**: 23.0.3 (Authentication & Authorization)
- **PostgreSQL**: Database
- **Flyway**: Database migrations
- **Lombok**: Reduce boilerplate
- **MapStruct**: Object mapping
- **JUnit 5, Mockito, AssertJ**: Testing

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 14+
- Keycloak 23.0.3 (or compatible version)

## Quick Start

### 1. Database Setup

Create a PostgreSQL database:

```bash
createdb keycloak_demo
```

### 2. Keycloak Setup

#### Start Keycloak (Docker):

```bash
docker run -d \
  --name keycloak \
  -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:23.0.3 \
  start-dev
```

#### Configure Keycloak:

1. Open Keycloak Admin Console: http://localhost:8080
2. Login with `admin/admin`
3. Create a new realm: `demo`
4. Create a client: `backend-client`
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Service Accounts Enabled: `ON`
   - Authorization Enabled: `ON`
5. Copy the client secret from the `Credentials` tab
6. Create a client for the frontend: `flutter-client`
   - Client Protocol: `openid-connect`
   - Access Type: `public`
   - Valid Redirect URIs: `http://localhost:3000/*`
   - Web Origins: `http://localhost:3000`

### 3. Configuration

Create an `application-local.yml` or set environment variables:

```yaml
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/keycloak_demo
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres

# Keycloak
KEYCLOAK_AUTH_SERVER_URL=http://localhost:8080
KEYCLOAK_REALM=demo
KEYCLOAK_CLIENT_ID=backend-client
KEYCLOAK_CLIENT_SECRET=your-client-secret-here

# JWT
KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/demo
KEYCLOAK_JWK_SET_URI=http://localhost:8080/realms/demo/protocol/openid-connect/certs

# Server
SERVER_PORT=8090
```

### 4. Build & Run

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run -pl host-application

# Or run with profile
mvn spring-boot:run -pl host-application -Dspring-boot.run.profiles=local
```

The application will start on http://localhost:8090

## API Endpoints

### Public Endpoints

#### Register User
```http
POST /api/v1/users/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response** (201 Created):
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "keycloakId": "abc123-def456"
}
```

### Protected Endpoints (Require JWT Token)

#### Get Current User
```http
GET /api/v1/users/me
Authorization: Bearer <JWT_TOKEN>
```

**Response** (200 OK):
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "keycloakId": "abc123-def456"
}
```

#### Get User by ID
```http
GET /api/v1/users/{id}
Authorization: Bearer <JWT_TOKEN>
```

**Response** (200 OK):
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "keycloakId": "abc123-def456"
}
```

## Authentication Flow

1. **Register**: User registers via `/api/v1/users/register` (creates user in both app DB and Keycloak)
2. **Login**: User logs in through Keycloak (outside this API)
3. **Access Protected Resources**: User sends JWT token in `Authorization: Bearer <token>` header

### Getting a JWT Token (for testing)

```bash
# Get access token from Keycloak
curl -X POST "http://localhost:8080/realms/demo/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=flutter-client" \
  -d "grant_type=password" \
  -d "username=johndoe" \
  -d "password=SecurePass123"
```

## Error Responses

All errors follow a consistent format:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request data",
  "path": "/api/v1/users/register",
  "details": [
    "Username must be between 3 and 50 characters",
    "Email must be valid"
  ]
}
```

### HTTP Status Codes

- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid input data
- `401 Unauthorized`: Missing or invalid authentication token
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource already exists (e.g., duplicate username/email)
- `500 Internal Server Error`: Server error

## Database Schema

### Schema: `demo_users_schema`

#### Table: `users`

| Column      | Type         | Constraints                    | Description                |
|-------------|--------------|--------------------------------|----------------------------|
| id          | UUID         | PRIMARY KEY                    | Internal user ID           |
| username    | VARCHAR(50)  | NOT NULL, UNIQUE               | Unique username            |
| email       | VARCHAR(255) | NOT NULL, UNIQUE               | User email                 |
| first_name  | VARCHAR(100) | NOT NULL                       | First name                 |
| last_name   | VARCHAR(100) | NOT NULL                       | Last name                  |
| keycloak_id | VARCHAR(255) | UNIQUE                         | Keycloak user ID (linked)  |

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Module Tests

```bash
# Test shared module
mvn test -pl shared

# Test user-domain module
mvn test -pl user-domain
```

### Test Coverage

- Domain model unit tests
- Value object validation tests
- Integration tests for REST endpoints (optional)

## Project Structure Details

### Shared Module
- **Value Objects**: `Email`, `UserId`
- **Exceptions**: `DomainException`, `ResourceNotFoundException`, `UserAlreadyExistsException`

### User Domain Module

#### Domain Layer (Pure, No Framework Dependencies)
- **Aggregate Root**: `User` - Rich domain model with business logic
- **Ports**: `UserRepository`, `KeycloakUserService` - Interfaces for adapters

#### Application Layer
- **Commands**: `RegisterUserCommand` - Write operations
- **Queries**: `UserQuery` - Read operations
- **Services**: `UserApplicationService` - Orchestrates use cases

#### Adapter Layer
- **REST API**: Controllers, DTOs, Mappers
- **Persistence**: JPA entities, Spring Data repositories, Adapters
- **Keycloak ACL**: Anti-Corruption Layer for Keycloak integration

### Host Application
- **Main Application**: Spring Boot entry point
- **Security Configuration**: OAuth2 JWT configuration
- **Database Migrations**: Flyway SQL scripts

## Design Principles

### Hexagonal Architecture
- **Domain Layer**: Pure business logic, no framework dependencies
- **Application Layer**: Orchestrates domain objects, transaction boundaries
- **Adapter Layer**: Implements ports, handles infrastructure concerns

### DDD Tactical Patterns
- **Aggregates**: `User` is an aggregate root with consistency boundary
- **Value Objects**: `Email`, `UserId` - immutable, validated
- **Repository Pattern**: Domain repository interface, implemented by adapter
- **Anti-Corruption Layer**: Protects domain from external system details (Keycloak)

### SOLID Principles
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Interfaces define contracts
- **Interface Segregation**: Small, focused interfaces
- **Dependency Inversion**: Depend on abstractions, not concretions

## Development Guidelines

1. **Domain Layer**: Keep pure - no Spring, JPA, or framework annotations
2. **Business Logic**: Belongs in domain models (User aggregate), not services
3. **Validation**: Domain validates business rules, adapters validate input format
4. **Mapping**: Use MapStruct for DTO ↔ Domain ↔ Persistence mapping
5. **Transactions**: Use `@Transactional` on application service methods
6. **Error Handling**: Domain throws domain exceptions, adapters handle HTTP status

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Check database credentials in `application.yml`
- Verify database exists: `psql -l`

### Keycloak Connection Issues
- Ensure Keycloak is running on port 8080
- Verify realm name matches configuration
- Check client secret is correct

### Authentication Issues
- Ensure JWT token is valid and not expired
- Verify `issuer-uri` and `jwk-set-uri` match Keycloak configuration
- Check token includes required claims (`preferred_username`)

### Build Issues
- Clean Maven cache: `mvn clean`
- Rebuild: `mvn clean install -DskipTests`
- Check Java version: `java -version` (should be 17+)

## License

MIT License

## Author

Demo Project - Keycloak Spring Boot Backend
