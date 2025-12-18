# Keycloak-Spring-Flutter Demo Project - Setup Guide

## Overview

This is a complete demo application showcasing authentication and authorization using:
- **Keycloak** (Authentication Provider) - Running in Docker
- **Spring Boot** (Backend/Middle-tier) - REST API with JPA
- **Flutter** (Frontend) - Mobile/Web app with Material Design 3
- **PostgreSQL** (Database) - Persistent storage

## Architecture

```
┌─────────────────┐
│  Flutter App    │  (Port 3000/8081)
│  - Register     │
│  - Login        │
│  - Welcome      │
└────────┬────────┘
         │ JWT Tokens
         ↓
┌─────────────────┐
│  Spring Boot    │  (Port 8082)
│  - REST API     │
│  - Security     │
│  - JPA          │
└────────┬────────┘
         │
         ↓
┌─────────────────┐     ┌──────────────┐
│  Keycloak       │────→│ PostgreSQL   │
│  (Port 8080)    │     │ (Port 5432)  │
└─────────────────┘     └──────────────┘
```

## Prerequisites

Before you begin, ensure you have the following installed:

- **Docker** (version 20+) and **Docker Compose** (version 2+)
- **Java** 17 or higher
- **Maven** 3.8 or higher
- **Flutter** 3.x SDK
- **Git**

### Verify Prerequisites

```bash
# Check Docker
docker --version
docker compose version

# Check Java
java -version

# Check Maven
mvn -version

# Check Flutter
flutter --version
flutter doctor
```

## Quick Start (5 Minutes)

### Step 1: Start Infrastructure (Docker)

```bash
# Start Keycloak and PostgreSQL
docker compose up -d

# Wait for services to be healthy (about 60 seconds)
docker compose ps

# Check Keycloak health
curl http://localhost:8080/health/ready

# Check PostgreSQL health
docker compose exec postgres pg_isready -U keycloak_user
```

**Keycloak Admin Console:**
- URL: http://localhost:8080
- Username: `admin`
- Password: `admin`

**Pre-configured Realm:**
- Realm: `demo-realm`
- Backend Client: `demo-backend` (Bearer-only)
- Flutter Client: `demo-flutter-app` (Public client)

### Step 2: Start Spring Boot Backend

```bash
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run -pl host-application

# The backend will start on http://localhost:8082
```

**Verify Backend:**
```bash
# Check actuator health endpoint
curl http://localhost:8082/actuator/health
```

### Step 3: Start Flutter Frontend

```bash
cd flutter_app

# Install dependencies
flutter pub get

# Create environment file
cp .env.example .env

# Edit .env and configure:
# - KEYCLOAK_URL=http://localhost:8080
# - KEYCLOAK_REALM=demo-realm
# - KEYCLOAK_CLIENT_ID=demo-flutter-app
# - BACKEND_URL=http://localhost:8082

# Run the app (choose platform)
flutter run -d chrome        # For web
flutter run -d macos         # For macOS
flutter run                  # For default device
```

**The app will open automatically and show the login page.**

## Complete Setup Details

### 1. Infrastructure Setup (Docker)

#### Docker Services Configuration

The `docker-compose.yml` defines two services:

1. **PostgreSQL Database**
   - Port: 5432
   - Database: `keycloak_demo`
   - User: `keycloak_user`
   - Password: `keycloak_password`
   - Schema: `demo_users_schema` (auto-created)

2. **Keycloak Server**
   - Port: 8080
   - Admin: `admin` / `admin`
   - Automatically imports `docker/realm-export.json`

#### Start Infrastructure

```bash
# Start all services
docker compose up -d

# View logs
docker compose logs -f

# Stop services
docker compose down

# Stop and remove volumes (clean slate)
docker compose down -v
```

#### Verify Infrastructure

```bash
# Check running containers
docker compose ps

# Check Keycloak logs
docker compose logs keycloak

# Check PostgreSQL logs
docker compose logs postgres

# Access PostgreSQL
docker compose exec postgres psql -U keycloak_user -d keycloak_demo
```

### 2. Keycloak Configuration

Keycloak is pre-configured via `docker/realm-export.json` with:

#### Realm: `demo-realm`
- Registration allowed
- Email as username
- Login with email allowed
- Password policy: 27500 hash iterations

#### Client: `demo-backend`
- Type: Bearer-only (Resource Server)
- Client ID: `demo-backend`
- Client Secret: `demo-backend-secret-change-in-production`
- Service Account: Enabled
- Token Lifespan: 300 seconds (5 minutes)

#### Client: `demo-flutter-app`
- Type: Public Client
- Client ID: `demo-flutter-app`
- Standard Flow: Enabled (OAuth2 Authorization Code)
- Direct Access Grants: Enabled (Resource Owner Password Credentials)
- Redirect URIs:
  - `http://localhost:3000/*`
  - `http://localhost:8081/*`
  - `myapp://callback`
  - `myapp://logout`
- Web Origins: `http://localhost:3000`, `http://localhost:8081`

#### Roles
- `user` (default role for all users)
- `admin` (administrator role)

#### Manual Keycloak Configuration (if needed)

If you need to reconfigure Keycloak manually:

1. Access Admin Console: http://localhost:8080
2. Login: `admin` / `admin`
3. Create Realm: `demo-realm`
4. Create Clients:
   - `demo-backend` (Bearer-only)
   - `demo-flutter-app` (Public)
5. Configure redirect URIs and CORS
6. Create roles: `user`, `admin`
7. Set `user` as default role

### 3. Spring Boot Backend Setup

#### Project Structure

```
backend/
├── shared/                          # Shared kernel
│   ├── Email.java                  # Email value object
│   ├── UserId.java                 # UserId value object
│   └── exceptions/                 # Domain exceptions
├── user-domain/                     # User bounded context
│   ├── domain/                     # Domain layer (pure)
│   │   ├── User.java               # User aggregate
│   │   ├── UserRepository.java     # Repository port
│   │   └── KeycloakService.java    # Keycloak port
│   ├── application/                # Application layer
│   │   ├── commands/               # Commands
│   │   ├── queries/                # Queries
│   │   └── UserApplicationService.java
│   ├── adapter/                    # Adapter layer
│   │   ├── in/rest/                # REST controllers
│   │   ├── out/persistence/        # JPA adapters
│   │   └── out/keycloak/           # Keycloak ACL
│   └── config/                     # Configuration
└── host-application/               # Main application
    ├── KeycloakDemoApplication.java
    ├── SecurityConfig.java
    └── resources/
        ├── application.yml
        └── db/migration/           # Flyway migrations
```

#### Build Backend

```bash
cd backend

# Clean and compile
mvn clean compile

# Run tests
mvn test

# Build all modules
mvn clean install

# Skip tests (faster)
mvn clean install -DskipTests
```

#### Configuration

Edit `backend/host-application/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/keycloak_demo
    username: keycloak_user
    password: keycloak_password
    
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/demo-realm
          
keycloak:
  admin:
    server-url: http://localhost:8080
    realm: demo-realm
    client-id: demo-backend
    client-secret: demo-backend-secret-change-in-production
```

#### Run Backend

```bash
# Option 1: Maven
mvn spring-boot:run -pl host-application

# Option 2: Java JAR
cd host-application
mvn clean package
java -jar target/host-application-1.0.0.jar
```

Backend will start on: http://localhost:8082

#### API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /api/v1/users/register | Public | Register new user |
| GET | /api/v1/users/me | JWT | Get current user |
| GET | /api/v1/users/{id} | JWT | Get user by ID |

#### Test Backend

```bash
# Register a new user
curl -X POST http://localhost:8082/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Password123!",
    "firstName": "Test",
    "lastName": "User"
  }'

# Get JWT token from Keycloak
curl -X POST http://localhost:8080/realms/demo-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=demo-flutter-app" \
  -d "username=testuser" \
  -d "password=Password123!" \
  -d "grant_type=password"

# Use token to get user info (replace TOKEN)
curl -X GET http://localhost:8082/api/v1/users/me \
  -H "Authorization: Bearer TOKEN"
```

### 4. Flutter Frontend Setup

#### Project Structure

```
flutter_app/
├── lib/
│   ├── main.dart                   # App entry point
│   ├── design_system/              # Design system
│   │   ├── design_tokens.dart      # Tokens
│   │   └── components/             # Atomic components
│   │       ├── atoms/              # Basic components
│   │       ├── molecules/          # Composed components
│   │       └── organisms/          # Complex components
│   ├── features/                   # Features (DDD)
│   │   └── auth/                   # Authentication feature
│   │       ├── data/               # Data layer
│   │       │   ├── models/         # DTOs
│   │       │   └── repositories/   # Repositories
│   │       └── presentation/       # UI layer
│   │           ├── pages/          # Screens
│   │           └── providers/      # State management
│   └── core/                       # Shared core
│       ├── config/                 # Configuration
│       ├── navigation/             # Routing
│       ├── network/                # HTTP client
│       ├── storage/                # Secure storage
│       └── utils/                  # Utilities
├── test/                           # Tests
└── pubspec.yaml                    # Dependencies
```

#### Install Dependencies

```bash
cd flutter_app

# Get dependencies
flutter pub get

# Check for issues
flutter doctor
```

#### Configuration

1. Create `.env` file:
```bash
cp .env.example .env
```

2. Edit `.env`:
```env
KEYCLOAK_URL=http://localhost:8080
KEYCLOAK_REALM=demo-realm
KEYCLOAK_CLIENT_ID=demo-flutter-app
BACKEND_URL=http://localhost:8082
```

#### Run Flutter App

```bash
# Run on Chrome (web)
flutter run -d chrome

# Run on macOS desktop
flutter run -d macos

# Run on iOS simulator
flutter run -d "iPhone 15"

# Run on Android emulator
flutter run -d emulator-5554

# List available devices
flutter devices
```

#### Build Flutter App

```bash
# Build web app
flutter build web

# Build macOS app
flutter build macos

# Build iOS app
flutter build ios

# Build Android APK
flutter build apk
```

#### Test Flutter App

```bash
# Run all tests
flutter test

# Run specific test
flutter test test/design_system/components/atoms/app_button_test.dart

# Run tests with coverage
flutter test --coverage

# View coverage (requires lcov)
genhtml coverage/lcov.info -o coverage/html
open coverage/html/index.html
```

## Usage Workflow

### 1. Register New User

1. Open Flutter app
2. Click "Don't have an account? Register"
3. Fill in registration form:
   - Username: `demouser`
   - Email: `demo@example.com`
   - Password: `DemoPass123!`
   - Confirm Password: `DemoPass123!`
   - First Name: `Demo`
   - Last Name: `User`
4. Click "Register"
5. On success, navigate to Login page

### 2. Login

1. Enter credentials:
   - Username: `demouser`
   - Password: `DemoPass123!`
2. Click "Login"
3. App fetches JWT token from Keycloak
4. App fetches user info from backend
5. Navigate to Welcome page

### 3. Welcome Page

- Displays user information
- Shows username, email, full name
- Logout button to end session

### 4. Logout

1. Click "Logout" button
2. Tokens cleared from secure storage
3. Navigate back to Login page

## Troubleshooting

### Keycloak Issues

**Problem: Keycloak not starting**
```bash
# Check logs
docker compose logs keycloak

# Restart service
docker compose restart keycloak

# Clean restart
docker compose down
docker volume rm test_keycloak-spring-flutter_postgres_data
docker compose up -d
```

**Problem: Realm not imported**
```bash
# Check if realm exists
curl http://localhost:8080/realms/demo-realm

# If not, manually import via Admin Console
# Or restart container
docker compose restart keycloak
```

### Backend Issues

**Problem: Database connection failed**
```bash
# Check PostgreSQL
docker compose ps postgres

# Check connection
docker compose exec postgres psql -U keycloak_user -d keycloak_demo -c "SELECT 1;"

# Check backend logs
cd backend
mvn spring-boot:run -pl host-application
# Look for connection errors
```

**Problem: Keycloak authentication failed**
```bash
# Verify Keycloak is running
curl http://localhost:8080/health/ready

# Verify realm exists
curl http://localhost:8080/realms/demo-realm

# Check client configuration in Keycloak Admin Console
# Ensure client secret matches application.yml
```

**Problem: Build fails**
```bash
# Clean Maven cache
mvn clean

# Rebuild with debug
mvn clean install -X

# Check Java version
java -version  # Should be 17+
```

### Flutter Issues

**Problem: Dependency conflicts**
```bash
# Clean and reinstall
flutter clean
flutter pub get

# Update dependencies
flutter pub upgrade
```

**Problem: Environment variables not loaded**
```bash
# Ensure .env file exists
ls -la flutter_app/.env

# Check file format (no quotes around values)
cat flutter_app/.env

# Hot restart app (R in terminal)
```

**Problem: Network connection failed**
```bash
# Check backend is running
curl http://localhost:8082/actuator/health

# Check CORS configuration in backend
# Verify BACKEND_URL in .env matches actual backend URL
```

**Problem: Token not persisted**
```bash
# iOS Simulator: Reset secure storage
# Android: Clear app data
# macOS: Check Keychain Access

# Or reinstall app
flutter clean
flutter run
```

### CORS Issues

**Problem: CORS errors in browser**

Backend already has CORS configured for:
- `http://localhost:3000`
- `http://localhost:8081`

If using different port:

1. Edit `backend/host-application/src/main/java/com/demo/keycloak/SecurityConfig.java`
2. Add your origin to `allowedOrigins`
3. Rebuild and restart backend

## Development Tips

### Hot Reload

**Flutter:**
- Press `r` in terminal for hot reload
- Press `R` for hot restart
- Press `q` to quit

**Spring Boot:**
- Use Spring Boot DevTools for auto-reload
- Or rebuild: `mvn compile -pl user-domain,host-application`

### Database Inspection

```bash
# Connect to database
docker compose exec postgres psql -U keycloak_user -d keycloak_demo

# Switch to schema
\c keycloak_demo
SET search_path TO demo_users_schema;

# View tables
\dt

# Query users
SELECT * FROM users;

# Exit
\q
```

### Keycloak Admin Tasks

**View Users:**
1. Admin Console → demo-realm → Users
2. View all registered users

**View Sessions:**
1. Admin Console → demo-realm → Sessions
2. View active user sessions

**View Events:**
1. Admin Console → demo-realm → Events
2. Enable event logging
3. View login/logout events

### Logs

```bash
# Docker logs
docker compose logs -f keycloak
docker compose logs -f postgres

# Backend logs
cd backend
mvn spring-boot:run -pl host-application
# Logs appear in console

# Flutter logs
flutter run
# Logs appear in console
```

## Security Notes

⚠️ **This is a DEMO project. For production:**

1. **Change all default passwords/secrets:**
   - Keycloak admin password
   - Database password
   - Keycloak client secrets

2. **Use HTTPS:**
   - Enable SSL in Keycloak
   - Use HTTPS for backend API
   - Configure proper certificates

3. **Secure token storage:**
   - Use flutter_secure_storage (already implemented)
   - Never log tokens
   - Implement token refresh

4. **Environment variables:**
   - Use proper secrets management
   - Never commit .env files
   - Use CI/CD secrets

5. **Production configuration:**
   - Enable Keycloak email verification
   - Configure SMTP for emails
   - Set up proper password policies
   - Enable rate limiting
   - Add monitoring and logging

## Next Steps

1. **Add More Features:**
   - User profile editing
   - Password reset flow
   - Email verification
   - User roles and permissions

2. **Improve Security:**
   - Implement refresh tokens
   - Add MFA support
   - Add rate limiting
   - Add audit logging

3. **Add Testing:**
   - Backend integration tests
   - Flutter widget tests
   - E2E tests with Flutter Driver
   - API contract tests

4. **Deploy:**
   - Containerize backend
   - Deploy to cloud (AWS, GCP, Azure)
   - Set up CI/CD pipeline
   - Configure production Keycloak

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Flutter Documentation](https://flutter.dev/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

## Support

For issues or questions:
1. Check this guide first
2. Review component-specific READMEs:
   - `backend/README.md`
   - `flutter_app/README.md`
3. Check application logs
4. Review Keycloak admin console
5. Inspect database state

---

**Happy Coding! 🚀**
