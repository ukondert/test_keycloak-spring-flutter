---
name: User Story
about: Erstelle eine neue User Story für ein Feature
title: '[STORY] '
labels: ['user-story', 'needs-refinement']
assignees: ''
---

## User Story

**Als** [Rolle]  
**möchte ich** [Ziel/Wunsch]  
**um** [Nutzen] zu erreichen.

---

## Kontext

### Epic
- [ ] **Epic:** [Epic-Name]

### Workflowphase
- [ ] **Workflowphase:** [Phase aus Story Map]

### Priorität
- [ ] Must-Have
- [ ] Should-Have
- [ ] Could-Have
- [ ] Won't-Have

### Story Points
**Schätzung:** [1/2/3/5/8/13/21]

---

## Design-Konzepte

### Domain-Driven Design (DDD)
- **Bounded Context:** [z.B. Fahrtenmanagement, Benutzerverwaltung, Benachrichtigungen]
- **Aggregate(s):** [z.B. Fahrt, Benutzer, Mitfahranfrage]
- **Domain Events:** [z.B. FahrtErstellt, AnfrageAkzeptiert]
- **Ubiquitous Language Begriffe:** [Schlüsselbegriffe aus der Fachdomäne]

### Component-Driven Design (CDD)
- **UI Komponenten:** [z.B. FahrtListe, FahrtDetails, AnfrageFormular]
- **Atomic Design Level:** [Atom/Molekül/Organismus/Template/Page]
- **Design Tokens:** [z.B. Colors, Spacing, Typography verwendet]

---

## Architektur & Implementierung

### Software-Architektur
- [ ] **Hexagonal Architecture** (Ports & Adapters)
- [ ] **Layered Architecture**
- [ ] **Microservices Architecture**
- [ ] **Event-Driven Architecture**
- [ ] Andere: [bitte angeben]

### Layer-Zuordnung
- **Domain Layer:** [Entities, Value Objects, Aggregates]
- **Application Layer:** [Use Cases, DTOs]
- **Infrastructure Layer:** [Repositories, External Services]
- **Interface Layer:** [Controllers, GraphQL Resolvers]

### API-First Approach
- [ ] **OpenAPI Spec erforderlich:** `docs/api/[context]/[resource].yaml`
- [ ] **GraphQL Schema erforderlich:** `docs/api/graphql/[context].graphql`
- [ ] **BFF (Backend-for-Frontend) erforderlich**
- [ ] API-Endpoint(s): [z.B. POST /api/v1/rides, GET /api/v1/rides/{id}]

---

## Conversation Points

1. [Gesprächspunkt 1 - offene Fragen, Klärungsbedarf]
2. [Gesprächspunkt 2]
3. [Gesprächspunkt 3]

---

## Akzeptanzkriterien

### Funktionale Kriterien
1. [ ] [Kriterium 1 - z.B. Benutzer kann Startort und Zielort eingeben]
2. [ ] [Kriterium 2 - z.B. System validiert Abfahrtszeit (muss in Zukunft liegen)]
3. [ ] [Kriterium 3]

### Nicht-Funktionale Kriterien
- [ ] **Performance:** [z.B. Response Time < 200ms]
- [ ] **Security:** [z.B. Authentifizierung erforderlich, RBAC]
- [ ] **Accessibility:** [z.B. WCAG 2.1 AA, Keyboard Navigation]
- [ ] **Testing:** [Unit Tests, Integration Tests, E2E Tests]

### DDD-spezifische Akzeptanzkriterien
- [ ] **Invarianten sichergestellt:** [z.B. availableSeats > 0]
- [ ] **Domain Events publiziert:** [z.B. FahrtErstellt bei erfolgreicher Erstellung]
- [ ] **Bounded Context Grenzen respektiert:** [keine direkten Dependencies]

---

## Technische Notizen

### Abhängigkeiten
- **User Stories:** [IDs von abhängigen Stories]
- **Technische Dependencies:** [z.B. OpenAPI Generator, Storybook]
- **Externe Services:** [z.B. Email-Service, Push-Notification-Service]

### Datenmodell
```
[Optional: Skizze oder Referenz zu Domain Model]
- Entity: [Name]
- Value Objects: [Liste]
- Beziehungen: [Beschreibung]
```

### API Contract (Vorschlag)
```yaml
# OpenAPI Spec Snippet (wird in Phase 2 detailliert)
paths:
  /api/v1/[resource]:
    post:
      summary: [Kurzbeschreibung]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Create[Resource]Request'
```

---

## Definition of Done

- [ ] User Story erfüllt INVEST-Kriterien (Independent, Negotiable, Valuable, Estimable, Small, Testable)
- [ ] Akzeptanzkriterien sind testbar formuliert
- [ ] Bounded Context und Aggregate sind identifiziert
- [ ] API-Contract (OpenAPI/GraphQL) ist spezifiziert
- [ ] Tests sind implementiert (Unit, Integration, E2E)
- [ ] Code Review durchgeführt
- [ ] Dokumentation aktualisiert (API Docs, README)
- [ ] Domain Events sind dokumentiert (falls vorhanden)


