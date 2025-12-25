# Keycloak Spring Boot & Flutter Demo 🚀

**A complete full-stack demo application showcasing modern authentication patterns.**

This project demonstrates enterprise-grade authentication and authorization using:
- 🔐 **Keycloak** - Authentication & Authorization Provider (Docker)
- ☕ **Spring Boot 3** - Backend Middle-tier (REST API + JPA + Hexagonal Architecture)
- 📱 **Flutter** - Cross-platform Frontend (Material Design 3 + Atomic Design)
- 🐘 **PostgreSQL** - Persistent Database

## 🎯 Key Features

✅ User Registration with Keycloak  
✅ OAuth2/JWT Authentication  
✅ Protected API Endpoints  
✅ Secure Token Storage  
✅ Clean Architecture (Hexagonal + DDD)  
✅ Component-Driven Design (Atomic Design)  
✅ Pre-configured Docker Infrastructure  
✅ Production-Ready Code Quality  

## 📚 Documentation

- **[PROJECT_SETUP.md](PROJECT_SETUP.md)** - Complete setup guide with troubleshooting
- **[backend/README.md](backend/README.md)** - Backend architecture & API details
- **[flutter_app/README.md](flutter_app/README.md)** - Flutter app structure & design system

## ⚡ Quick Start (5 Minutes)

```bash
# 1. Start Infrastructure (Keycloak + PostgreSQL)
docker compose up -d

# 2. Start Backend (in new terminal)
cd backend && mvn spring-boot:run -pl host-application

# 3. Start Flutter App (in new terminal)
cd flutter_app && flutter pub get && flutter run -d chrome
```

**That's it!** Open your browser and start using the app.

## 📁 Project Structure

```
.
├── backend/         # Spring Boot backend (Java 17, Hexagonal Architecture, DDD)
├── flutter_app/     # Flutter frontend (Atomic Design, Provider, Material Design 3)
├── docker/          # Docker configuration for Keycloak & PostgreSQL
├── docker-compose.yml
└── PROJECT_SETUP.md # Detailed setup guide
```

## 🏗️ Architecture

```
┌─────────────────┐
│  Flutter App    │  Material Design 3
│  - Register     │  Port: 3000/8081
│  - Login        │  (Atomic Design)
│  - Welcome      │
└────────┬────────┘
         │ OAuth2 + JWT Tokens
         ↓
┌─────────────────┐
│  Spring Boot    │  Hexagonal Architecture
│  - REST API     │  Port: 8082
│  - Security     │  (DDD Patterns)
│  - JPA          │
└────────┬────────┘
         │
         ↓
┌─────────────────┐     ┌──────────────┐
│  Keycloak       │────→│ PostgreSQL   │
│  Auth Server    │     │  Database    │
│  Port: 8080     │     │  Port: 5432  │
└─────────────────┘     └──────────────┘
```

## 🛠️ Technology Stack

| Layer | Technology | Details |
|-------|------------|---------|
| **Backend** | Spring Boot 3.2.0 | Java 17, Maven Multi-module |
| | Spring Security | OAuth2 + JWT |
| | Spring Data JPA | Flyway Migrations |
| **Frontend** | Flutter 3.x | Dart 3.x |
| | Provider | State Management |
| | go_router | Navigation with Guards |
| | Material Design 3 | UI Framework |
| **Auth** | Keycloak 23.0.3 | Pre-configured Realm |
| **Database** | PostgreSQL 15 | Schema-per-module |
| **Infra** | Docker Compose | Orchestration |

## 📋 API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/users/register` | Public | Register new user |
| GET | `/api/v1/users/me` | JWT | Get current authenticated user |
| GET | `/api/v1/users/{id}` | JWT | Get user by ID |

## 🎯 User Flow

1. **Register** → Create account in Keycloak via backend API
2. **Login** → Authenticate with Keycloak, receive JWT tokens
3. **Welcome** → Access protected page, view user profile
4. **Logout** → Clear tokens, return to login

## 🔧 Backend Architecture

**Hexagonal Architecture (Ports & Adapters) + DDD**

```
backend/
├── shared/           # Shared Kernel
│   ├── Email        # Value Object
│   └── UserId       # Value Object
├── user-domain/      # User Bounded Context
│   ├── domain/      # Pure domain logic
│   ├── application/ # Use cases
│   └── adapter/     # REST, JPA, Keycloak
└── host-application/ # Main app
```

[➡️ Backend Documentation](./backend/README.md)

## 🎨 Frontend Architecture

**Atomic Design + Feature-First + Provider**

```
flutter_app/
├── design_system/    # Design Tokens + Components
│   ├── atoms/       # Basic components
│   ├── molecules/   # Composed components
│   └── organisms/   # Complex components
├── features/auth/    # Authentication feature
│   ├── data/        # DTOs, Repositories
│   └── presentation/ # Pages, Providers
└── core/            # Navigation, HTTP, Storage
```

[➡️ Flutter App Documentation](./flutter_app/README.md)

## 🧪 Testing

```bash
# Backend tests (15 unit tests)
cd backend && mvn test

# Flutter tests
cd flutter_app && flutter test

# API testing (after starting services)
curl -X POST http://localhost:8082/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","email":"demo@example.com","password":"Demo123!","firstName":"Demo","lastName":"User"}'
```

## 🚨 Troubleshooting

**Keycloak not starting?**
```bash
docker compose logs keycloak
docker compose restart keycloak
```

**Backend connection issues?**
```bash
docker compose exec postgres pg_isready
curl http://localhost:8080/health/ready
```

**Flutter build issues?**
```bash
flutter clean && flutter pub get
flutter doctor
```

**See [PROJECT_SETUP.md](PROJECT_SETUP.md) for detailed troubleshooting.**

## ⚠️ Security Notes

This is a **DEMO** project. For production:
- ✅ Change default passwords and secrets
- ✅ Enable HTTPS/SSL
- ✅ Use proper secrets management
- ✅ Enable Keycloak email verification
- ✅ Implement token refresh
- ✅ Add rate limiting and monitoring

## 📖 Learning Resources

- [Spring Boot Security](https://spring.io/guides/topicals/spring-security-architecture)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Flutter Authentication](https://flutter.dev/docs/cookbook/networking/authenticated-requests)
- [DDD Patterns](https://martinfowler.com/tags/domain%20driven%20design.html)

---

# Original Template Documentation

Template für die systematische Umsetzung von Softwareprojekten mit Domain-Driven Design (DDD) und AI-gestützten Chatmodes.

## Überblick

Dieses Repository bietet einen **vollständigen Workflow** von der Anforderungserhebung bis zur Implementierung. Durch strukturierte AI-Chatmodes werden Sie schrittweise durch alle Phasen eines Softwareprojekts geführt:

1. **Requirements Engineering** - Interviews & Workshops zur Anforderungserhebung
2. **Software Architecture** - Domain-Modellierung mit DDD
3. **Software Development** - API-First Implementierung mit Clean Architecture

Jeder Chatmode erstellt spezifische Dokumente, die nahtlos ineinander greifen und eine vollständige Projektdokumentation bilden.

## Chatmode-Workflow

### Phase 1: Requirements Engineering

**Chatmode:** `requirements-engineer.chatmode.md`

Führt durch die systematische Anforderungserhebung vom Projektkontext bis zur Story Map:

1. **Projektkontext & Stakeholder-Identifikation**
2. **Informationserhebung** (nutzt unterstützende Chatmodes):
   - `request-interview.chatmode.md` - Simulierte Stakeholder-Interviews
   - `request-workshop-moderator.chatmode.md` - Moderierte Workshops
   - `request-workshop-stakeholder.chatmode.md` - Stakeholder-Perspektiven
3. **Story Mapping** - Erstellung einer User Story Map
4. **User Story Erstellung** - Dokumentation aller Stories

**Erstellt folgende Dokumente:**
```
docs/requirements/
  ├── transcripts/
  │   ├── interview_*.md              # Interview-Transkripte
  │   └── workshop_summary.md         # Workshop-Zusammenfassungen
  ├── story-map.md                    # User Story Map
  ├── stakeholder-overview.md         # Stakeholder-Übersicht
  └── user-stories/
      └── user-story-*.md             # Alle User Stories
```

### Phase 2: Software Architecture

**Chatmodes:** DDD Architecture Workflow mit Handoffs (siehe [docs/chatmodes-workflow.md](docs/chatmodes-workflow.md))

Transformiert User Stories in ein vollständiges Domain-Modell durch drei strukturierte Phasen:

#### Phase 2.1: Strategic Design
**Chatmode:** `ddd-architect-strategic-design.chatmode.md`

1. **Domain Analysis** - Identifikation von Bounded Contexts
2. **Ubiquitous Language** - Extraktion von Nomen, Verben und Geschäftsregeln
3. **Domain Categorization** - Core Domain, Supporting, Generic Subdomain
4. **Context Mapping** - Beziehungen zwischen Bounded Contexts

**➡️ Handoff:** `ddd-architect-taktik-design` (Taktisches Design starten)

#### Phase 2.2: Tactical Design
**Chatmode:** `ddd-architect-taktik-design.chatmode.md`

1. **DDD Building Blocks** - Aggregates, Entities, Value Objects identifizieren
2. **Attribute & Beziehungen** - Detailliertes Modellieren
3. **Geschäftslogik** - Methoden den richtigen Aggregaten zuordnen
4. **Domain Services** - Aggregatübergreifende Logik

**➡️ Handoff:** `ddd-architect-visual-design` (Visualisierung starten)

#### Phase 2.3: Visual Design
**Chatmode:** `ddd-architect-visual-design.chatmode.md`

1. **Glossar** - Vollständiges Ubiquitous Language Glossar
2. **PlantUML Diagrams** - Visuelle Darstellung pro Bounded Context
3. **Domain Model Documentation** - Umfassende Gesamt-Dokumentation
4. **Traceability Matrix** - User Stories ↔ Aggregate Mapping

**➡️ Handoff:** `sw-developer` (Implementierung starten)

**Erstellt folgende Dokumente:**
```
docs/architecture/
  ├── bounded-contexts-overview.md    # Übersicht der Bounded Contexts
  ├── domain-categorization.md        # Kategorisierung (Core/Supporting/Generic)
  ├── aggregates-entities-valueobjects.md  # DDD Building Blocks
  ├── ubiquitous-language-glossar.md  # Vollständiges Glossar
  ├── ubiquitous-language_nomen&verben.md  # Initiale Extraktion
  ├── traceability-matrix.md          # Nachverfolgbarkeit
  ├── domain-model.md                 # Vollständige Dokumentation
  └── domain-models/
      └── *.domain-model.puml         # PlantUML Domain Models
```

> **💡 Hinweis:** Die Handoff-Funktionalität (automatische Übergänge zwischen Chatmodes) ist aktuell nur in **VS Code Insiders** verfügbar. In VS Code Stable verwenden Sie die expliziten Texthinweise am Ende jeder Phase.

### Phase 3: Software Development

#### Phase 3a: Backend Development

**Chatmode:** `sw-developer.chatmode.md`

Implementiert User Stories iterativ mit API-First Approach und Clean Architecture:

1. **Story Selection** - Auswahl der zu implementierenden Story
2. **API Design** - OpenAPI 3.0 Spezifikation
3. **Domain Layer** - Value Objects, Entities, Aggregates
4. **Application Layer** - Use Cases, DTOs, Ports
5. **Infrastructure Layer** - Repositories, External Services
6. **Interface Layer** - REST Controllers, GraphQL Resolver
7. **Testing** - Unit, Integration, E2E, BDD
8. **Documentation** - API Docs, Code Comments, ADRs

**Technologie-Stack:**
- TypeScript/Node.js mit NestJS
- Clean/Hexagonal Architecture
- DDD Tactical Patterns
- TDD/BDD Testing
- OpenAPI/Swagger

#### Phase 3b: Frontend Development

**Chatmode:** `sw-frontend-developer.chatmode.md`

Implementiert User Stories iterativ mit Component-Driven Development und Design System Integration:

1. **UX Analysis** - Benutzerfluss, Screens, States, Accessibility
2. **UI Specification** - Komponenten-Design, Props, Responsive Behavior
3. **API Client Layer** - OpenAPI Type Generation, Server State Management
4. **State & Interaction** - Form Handling, Optimistic Updates, Keyboard Navigation
5. **Component Implementation** - Atomic Design, Design Tokens, Styling
6. **Testing** - Unit, Component (a11y), Visual Regression, E2E
7. **Performance** - Code Splitting, Bundle Analysis, Web Vitals
8. **Documentation** - Storybook, Prop Tables, Review Checklist

**Technologie-Stack:**
- React/Next.js/Vue/Svelte
- Storybook + Design Tokens
- CSS Modules/Tailwind/Styled Components
- React Query/Zustand für State Management
- react-hook-form + Zod für Forms
- Vitest/Jest + Testing Library + Playwright
- WCAG 2.1 AA Accessibility Standards

**Erstellt folgende Artefakte:**
```
src/
  ├── components/                    # Component-Driven Development
  │   └── [Component]/
  │       ├── [Component].tsx
  │       ├── [Component].test.tsx
  │       ├── [Component].stories.tsx
  │       └── styles.module.css
  ├── api/
  │   ├── types/                     # OpenAPI Generated Types
  │   └── clients/                   # API Client Wrappers
  ├── state/
  │   └── queries/                   # Server State (React Query)
  ├── design/
  │   └── tokens.ts                  # Design Tokens
  └── ui/
      └── patterns/                  # UI Patterns (Spinner, ErrorBoundary)
ui-spec/                             # UI Specifications
  └── [story-id].md
analysis/                            # Frontend Analysis
  └── [story-id]-frontend-analysis.md
tests/e2e/                          # Playwright E2E Tests
```

### Unterstützende Chatmodes

- **request-tec-crc.chatmode.md** - CRC-Card Sessions für OO-Analyse
- **request-interview.chatmode.md** - Stakeholder-Interviews
- **request-workshop-moderator.chatmode.md** - Workshop-Moderation
- **request-workshop-stakeholder.chatmode.md** - Stakeholder-Simulation

## Quick Start

### 1. Requirements Engineering starten

Aktiviere den Chatmode `.github/chatmodes/requirements-engineer.chatmode.md`:

```
Ich möchte ein neues Softwareprojekt starten. 
Die Projektidee ist: [IHRE PROJEKTIDEE]
```

Der Chatmode führt Sie durch:
- Stakeholder-Identifikation
- Interview-/Workshop-Durchführung
- Story Map Erstellung
- User Story Dokumentation

### 2. Software Architecture erstellen

Aktiviere den Chatmode `.github/chatmodes/sw-architect.chatmode.md`:

```
Analysiere die User Stories und erstelle das Domain Model.
Basis: docs/requirements/user-stories/*.md
```

Der Chatmode erstellt:
- Bounded Contexts
- Domain Models (PlantUML)
- Ubiquitous Language
- Architektur-Entscheidungen
- Verfeinerte User Stories

### 3. Software Development durchführen

#### Backend Implementation

Aktiviere den Chatmode `.github/chatmodes/sw-developer.chatmode.md`:

```
Implementiere die Story: [STORY-NAME]
Basis: docs/requirements/user-stories/refined/[STORY].md
```

Der Chatmode implementiert:
- OpenAPI Spezifikation
- Domain Layer (DDD Patterns)
- Application Layer (Use Cases)
- Infrastructure Layer (Repositories)
- Interface Layer (REST/GraphQL)
- Tests (Unit/Integration/E2E)

#### Frontend Implementation

Aktiviere den Chatmode `.github/chatmodes/sw-frontend-developer.chatmode.md`:

```
Implementiere User Story US-010 (Profil bearbeiten)
Basis: docs/requirements/user-stories/refined/[STORY].md
```

Der Chatmode implementiert:
- UI Spec & Component Design
- API Client Generation (OpenAPI Types)
- React Components mit Design Tokens
- State Management (React Query)
- Storybook Stories
- Tests (Unit/Component/E2E/A11y)
- Performance Optimierung

## Dokument-Struktur

```
docs/
├── requirements/                    # Requirements Engineering Outputs
│   ├── transcripts/                # Interview & Workshop Transkripte
│   │   ├── interview_*.md
│   │   └── workshop_summary.md
│   ├── user-stories/               # User Stories
│   │   ├── user-story-*.md
│   │   └── refined/                # Technisch verfeinerte Stories
│   │       └── user-story-refinement-*.md
│   ├── story-map.md                # User Story Map
│   ├── stakeholder-overview.md     # Stakeholder-Übersicht
│   ├── mvp-summary.md              # MVP Definition
│   └── epics.md                    # Epic-Übersicht
│
└── architecture/                    # Software Architecture Outputs
    ├── bounded-contexts-overview.md
    ├── domain-categorization.md
    ├── agregates-entites-value_obj.md
    ├── architecture-decisions.md
    ├── ubiquitous-language-glossar.md
    ├── ubiquitous-language_nomen&verben.md
    ├── traceability-matrix.md
    └── domain-models/              # PlantUML Domain Models
        ├── *.domain-model.puml
        └── domain-model.md
```

## Konventionen & Best Practices

Das Template enthält Standards für konsistente Entwicklung:

- **Naming Conventions**: `.agent-resources/definitions-conventions/naming-conventions.md`
  - DDD Naming (Value Objects, Entities, Aggregates, etc.)
  - TypeScript/Node.js Konventionen
  
- **Testing Strategy**: `.agent-resources/definitions-conventions/testing-strategy.md`
  - Unit Testing (Domain Layer)
  - Integration Testing (Application/Infrastructure)
  - E2E Testing (Interface Layer)
  - BDD mit Gherkin

## Methodologie

Dieses Template basiert auf:

- **Domain-Driven Design (DDD)**
  - Strategic Design: Bounded Contexts, Ubiquitous Language
  - Tactical Design: Aggregates, Entities, Value Objects
  
- **Clean/Hexagonal Architecture**
  - Domain Layer (Business Logic)
  - Application Layer (Use Cases)
  - Infrastructure Layer (Technical Details)
  - Interface Layer (API/UI)
  
- **API-First Development**
  - OpenAPI 3.0 Spezifikation vor Implementierung
  - Contract-First Approach
  - Automatische Validierung & Dokumentation
  
- **Test-Driven Development (TDD)**
  - Red-Green-Refactor Cycle
  - Behavior-Driven Development (BDD)
  - Multi-Level Testing Strategy

## Beispiel-Workflow

1. **Projekt initialisieren**
   ```
   Chatmode: requirements-engineer.chatmode.md
   Input: Projektidee
   Output: docs/requirements/
   ```

2. **Domain modellieren**
   ```
   Chatmode: sw-architect.chatmode.md
   Input: docs/requirements/user-stories/*.md
   Output: docs/architecture/
   ```

3. **Story implementieren**
   ```
   Backend:
   Chatmode: sw-developer.chatmode.md
   Input: docs/requirements/user-stories/refined/user-story-*.md
   Output: src/, tests/, docs/api/
   
   Frontend:
   Chatmode: sw-frontend-developer.chatmode.md
   Input: docs/requirements/user-stories/refined/user-story-*.md
   Output: src/components/, src/api/, ui-spec/, tests/e2e/
   ```

4. **Nächste Story**
   ```
   Wiederhole Schritt 3 für jede Story
   ```

## Repository Structure

```
template-bmad_method_ext/
├── README.md                           # Diese Datei
├── requirements.md                     # Anforderungen an das Template
├── .github/chatmodes/                  # Chatmode Definitionen
│   ├── requirements-engineer.chatmode.md
│   ├── sw-architect.chatmode.md
│   ├── sw-developer.chatmode.md
│   ├── sw-frontend-developer.chatmode.md
│   ├── request-interview.chatmode.md
│   ├── request-workshop-moderator.chatmode.md
│   ├── request-workshop-stakeholder.chatmode.md
│   └── request-tec-crc.chatmode.md
├── .agent-resources/
│   └── definitions-conventions/        # Entwicklungsstandards
│       ├── naming-conventions.md       # DDD Naming
│       └── testing-strategy.md         # Testing Best Practices
├── chatmodes/                          # Dokumentation
│   ├── README.md
│   └── EXAMPLES.md
└── docs/                               # Projekt-Dokumentation
    ├── requirements/                   # Von requirements-engineer erstellt
    └── architecture/                   # Von sw-architect erstellt
```

---

**Ready to get started?** Beginnen Sie mit dem `requirements-engineer.chatmode.md` und lassen Sie sich durch den kompletten Entwicklungsprozess führen!
