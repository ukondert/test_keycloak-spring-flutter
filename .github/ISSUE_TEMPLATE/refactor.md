---
name: Refactoring
about: Code-Verbesserung ohne Funktionsänderung (Architektur, Performance, Wartbarkeit)
title: '[REFACTOR] '
labels: ['refactoring', 'technical-debt']
assignees: ''
---

## Refactoring-Ziel

**Was soll verbessert werden?**
[Kurze Beschreibung des Refactoring-Ziels]

**Warum ist diese Änderung notwendig?**
[Begründung: Technical Debt, Performance, Wartbarkeit, Architektur-Alignment]

---

## Kontext

### Betroffene Bereiche
- [ ] **Bounded Context:** [z.B. Fahrtenmanagement, Benutzerverwaltung]
- [ ] **Layer:** Domain / Application / Infrastructure / Interface
- [ ] **Dateien/Module:** [Liste der betroffenen Dateien]

### Priorität
- [ ] Kritisch (blockiert andere Arbeit)
- [ ] Hoch (Technical Debt reduzieren)
- [ ] Mittel (Wartbarkeit verbessern)
- [ ] Niedrig (Nice-to-have)

### Aufwandsschätzung
**Story Points:** [1/2/3/5/8/13]

---

## Design-Konzepte & Architektur

### Domain-Driven Design (DDD)
- [ ] **Aggregate Refactoring:** [z.B. Fahrt-Aggregate in kleinere Aggregates aufteilen]
- [ ] **Ubiquitous Language Alignment:** [Umbenennung gemäß Fachsprache]
- [ ] **Domain Event Einführung:** [z.B. Events für Zustandsänderungen publizieren]
- [ ] **Value Object Extraktion:** [z.B. Email, PhoneNumber als Value Objects]
- [ ] **Repository Pattern Verbesserung:** [z.B. Query-Methoden optimieren]

### Architektur-Patterns
- [ ] **Hexagonal Architecture Alignment**
  - Domain Layer: Abhängigkeiten entfernen
  - Application Layer: Use Case Orchestrierung verbessern
  - Infrastructure Layer: Framework-Details kapseln
  - Interface Layer: DTO-Mapping optimieren
- [ ] **Event-Driven Architecture:** [Domain Events einführen/verbessern]
- [ ] **CQRS Pattern:** [Command/Query Separation]
- [ ] **Andere:** [bitte angeben]

### API-Design
- [ ] **OpenAPI Spec Verbesserung:** [z.B. Konsistenz, Versionierung, Error Handling]
- [ ] **GraphQL Schema Refactoring:** [z.B. Type-Optimierung, Resolver-Struktur]
- [ ] **BFF Optimierung:** [z.B. Aggregation verbessern, Caching]
- [ ] **RESTful Principles:** [z.B. Resource Naming, HTTP Methods korrekt nutzen]

### Component-Driven Design (Frontend)
- [ ] **Component Extraktion:** [z.B. Wiederverwendbare Komponenten extrahieren]
- [ ] **Atomic Design Alignment:** [z.B. korrekte Atom/Molekül/Organismus Zuordnung]
- [ ] **Props/State Refactoring:** [z.B. Prop Drilling reduzieren, State-Lifting]
- [ ] **Design Token Migration:** [z.B. Hardcoded Werte durch Tokens ersetzen]

---

## Refactoring-Details

### Ist-Zustand (Current State)
```
[Code-Beispiel oder Beschreibung des aktuellen Zustands]

Probleme:
- [Problem 1: z.B. Zyklische Dependencies zwischen Aggregates]
- [Problem 2: z.B. Business Logic in Controller-Layer]
- [Problem 3: z.B. Performance-Bottleneck durch N+1 Queries]
```

### Soll-Zustand (Target State)
```
[Code-Beispiel oder Beschreibung des gewünschten Zustands]

Verbesserungen:
- [Verbesserung 1: z.B. Klare Aggregate-Grenzen gemäß DDD]
- [Verbesserung 2: z.B. Business Logic in Domain Layer verschoben]
- [Verbesserung 3: z.B. Eager Loading oder Caching implementiert]
```

### Refactoring-Strategie
- [ ] **Strangler Fig Pattern:** [Alten Code schrittweise ersetzen]
- [ ] **Feature Flag:** [Neue Implementierung parallel laufen lassen]
- [ ] **Big Bang:** [Alle Änderungen auf einmal]
- [ ] **Branch by Abstraction:** [Abstraktionsschicht einführen]

---

## Auswirkungen

### Breaking Changes
- [ ] **Keine Breaking Changes**
- [ ] **API Breaking Changes:** [Details - erfordert Versionierung]
- [ ] **Database Migration erforderlich**
- [ ] **Konfigurationsänderungen erforderlich**

### Betroffene Komponenten
- **Domain Layer:** [z.B. Fahrt-Aggregate, RideRequest-Entity]
- **Application Layer:** [z.B. CreateRideUseCase, AcceptRequestUseCase]
- **Infrastructure Layer:** [z.B. RideRepository, EventBus]
- **Interface Layer:** [z.B. RideController, GraphQL Resolver]
- **Frontend:** [z.B. RideList Component, RequestForm]

### Test-Strategie
- [ ] **Bestehende Tests müssen angepasst werden**
- [ ] **Neue Tests erforderlich für:** [z.B. neue Domain Events]
- [ ] **Contract Tests aktualisieren:** [OpenAPI/GraphQL Schema Änderungen]
- [ ] **E2E Tests durchführen**

---

## Akzeptanzkriterien

### Funktionale Kriterien
- [ ] Alle bestehenden Features funktionieren unverändert
- [ ] Keine Regression in User Stories
- [ ] API-Kompatibilität gewährleistet (oder Versionierung implementiert)

### Nicht-Funktionale Kriterien
- [ ] **Code Quality:** [z.B. Code Smells entfernt, Complexity reduziert]
- [ ] **Performance:** [z.B. Response Time Verbesserung messbar]
- [ ] **Maintainability:** [z.B. Cyclomatic Complexity < 10]
- [ ] **Test Coverage:** [z.B. mindestens gleich bleibend oder verbessert]

### Architektur-Kriterien
- [ ] **DDD Patterns korrekt angewendet:** [z.B. Aggregate Invarianten gewahrt]
- [ ] **Layer Separation eingehalten:** [z.B. Domain hat keine Infrastructure-Dependencies]
- [ ] **Ubiquitous Language konsistent:** [Code reflektiert Fachsprache]
- [ ] **Domain Events dokumentiert:** [z.B. in docs/architecture/domain-events.md]

---

## Technische Notizen

### Abhängigkeiten
- **Blockiert durch:** [andere Issues/PRs]
- **Blockiert:** [andere Issues/PRs]
- **Related Issues:** [verwandte Refactorings]

### Migration-Schritte
1. [Schritt 1: z.B. Neue Abstraktionsschicht einführen]
2. [Schritt 2: z.B. Tests auf neue Implementierung migrieren]
3. [Schritt 3: z.B. Alte Implementierung entfernen]
4. [Schritt 4: z.B. Dokumentation aktualisieren]

### Rollback-Plan
```
[Beschreibung wie Änderungen rückgängig gemacht werden können]
- Feature Flag deaktivieren
- Database Migration rückwärts laufen lassen
- Code-Revert via Git
```

---

## Definition of Done

- [ ] Code Review durchgeführt
- [ ] Alle Tests (Unit, Integration, E2E) erfolgreich
- [ ] Performance-Messungen durchgeführt (falls relevant)
- [ ] Dokumentation aktualisiert (Architecture Decisions, API Docs)
- [ ] Ubiquitous Language Glossar aktualisiert (falls Begriffe geändert)
- [ ] Domain Model Diagramme aktualisiert (falls Struktur geändert)
- [ ] Breaking Changes dokumentiert (CHANGELOG.md)
- [ ] Code Coverage mindestens gleichbleibend

---

## Referenzen

- **Architecture Decisions:** `docs/architecture/architecture-decisions.md`
- **Domain Model:** `docs/architecture/domain-models/[context].puml`
- **Hexagonal Architecture Guide:** `.agent-resources/architecture/layers/hexagonal-architecture.md`
- **DDD Best Practices:** `.agent-resources/best-practices/ddd.best-practices.md`
- **API-First Guide:** `.agent-resources/best-practices/api-first-ddd-guide.md`
- **Naming Conventions:** `.agent-resources/definitions-conventions/naming-conventions.md`

---

*Hinweis: Dokumentiere Architektur-Entscheidungen als ADR (Architecture Decision Record) in `docs/architecture/decisions/`*
