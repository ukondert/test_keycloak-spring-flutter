---
name: Bug Report
about: Melde einen Fehler zur Behebung
title: '[BUG] '
labels: ['bug', 'needs-triage']
assignees: ''
---

## Bug-Beschreibung

**Was ist das Problem?**
[Klare und präzise Beschreibung des Fehlers]

**Erwartetes Verhalten:**
[Was sollte passieren?]

**Tatsächliches Verhalten:**
[Was passiert stattdessen?]

---

## Reproduktion

### Schritte zur Reproduktion
1. [Schritt 1: z.B. Navigiere zu /rides]
2. [Schritt 2: z.B. Klicke auf "Fahrt erstellen"]
3. [Schritt 3: z.B. Gib ungültige Abfahrtszeit ein]
4. [Schritt 4: z.B. Klicke auf "Speichern"]

### Reproduzierbarkeit
- [ ] Immer reproduzierbar
- [ ] Manchmal reproduzierbar
- [ ] Selten reproduzierbar
- [ ] Einmalig aufgetreten

### Umgebung
- **Browser/Client:** [z.B. Chrome 120, Safari 17, Mobile App iOS 17]
- **Backend:** [z.B. Node.js 20.x, Java 21]
- **Datenbank:** [z.B. PostgreSQL 16, MongoDB 7.0]
- **Environment:** [Development / Staging / Production]

---

## Kontext

### Betroffene Bereiche

#### Domain-Driven Design (DDD)
- **Bounded Context:** [z.B. Fahrtenmanagement, Benutzerverwaltung]
- **Aggregate:** [z.B. Fahrt, Benutzer, Mitfahranfrage]
- **Domain Event:** [z.B. FahrtErstellt wird nicht publiziert]
- **Business Rule verletzt:** [z.B. Invariante "availableSeats > 0" nicht geprüft]

#### Software-Architektur Layer
- [ ] **Domain Layer** (Entities, Value Objects, Aggregates)
- [ ] **Application Layer** (Use Cases, DTOs)
- [ ] **Infrastructure Layer** (Repositories, External Services)
- [ ] **Interface Layer** (Controllers, GraphQL Resolvers)
- [ ] **Frontend** (UI Components, State Management)

#### API-Kontext
- **Endpoint:** [z.B. POST /api/v1/rides]
- **GraphQL Query/Mutation:** [z.B. mutation createRide]
- **BFF betroffen:** [z.B. ja/nein]
- **OpenAPI Spec:** [Referenz zu docs/api/[context]/[resource].yaml]

---

## Technische Details

### Fehlermeldungen
```
[Error-Logs, Stack Traces, Console Errors]

Beispiel:
Error: Validation failed: departureTime must be in the future
  at RideAggregate.create (ride.aggregate.ts:45)
  at CreateRideUseCase.execute (create-ride.usecase.ts:28)
```

### Code-Referenz
**Datei:** [z.B. `src/domain/rides/ride.aggregate.ts`]
**Zeile:** [z.B. Zeile 45]

```typescript
// Problematischer Code
const ride = Ride.create({
  departureTime: new Date(dto.departureTime),
  // Fehlende Validierung: departureTime > now()
});
```

### Root Cause Analysis
- [ ] **Fehlende Validierung** (Domain Layer)
- [ ] **Falsche Business Logic** (Use Case)
- [ ] **Datenbank-Inkonsistenz** (Repository/Infrastructure)
- [ ] **API-Contract Verletzung** (OpenAPI Spec nicht eingehalten)
- [ ] **Race Condition** (Concurrency Problem)
- [ ] **Fehlendes Domain Event** (Event nicht publiziert)
- [ ] **Frontend State Management** (React/Vue/Angular State Bug)
- [ ] **Andere:** [bitte beschreiben]

---

## Auswirkungen

### Schweregrad
- [ ] **Kritisch** (System/Feature nicht nutzbar, Datenverlust)
- [ ] **Hoch** (Wichtige Features betroffen, Workaround möglich)
- [ ] **Mittel** (Features eingeschränkt nutzbar)
- [ ] **Niedrig** (Kosmetischer Fehler, keine Funktionsbeeinträchtigung)

### Betroffene User Stories
- [ ] **User Story:** [ID/Link - z.B. #123 "Fahrt anbieten"]
- [ ] **Epic:** [z.B. Fahrten-Management]
- [ ] **Akzeptanzkriterium verletzt:** [z.B. "System validiert Abfahrtszeit"]

### Auswirkungen auf Architektur
- [ ] **Invariante verletzt:** [z.B. Aggregate in inkonsistentem Zustand]
- [ ] **Domain Event fehlt:** [z.B. Nachfolge-Prozesse nicht ausgelöst]
- [ ] **Bounded Context Grenze verletzt:** [z.B. direkte Dependency statt Event]
- [ ] **API-Contract gebrochen:** [z.B. OpenAPI Spec nicht eingehalten]

---

## Lösungsansatz

### Vorgeschlagene Lösung
```typescript
// Vorgeschlagener Fix
const ride = Ride.create({
  departureTime: new Date(dto.departureTime),
});

// Validierung hinzufügen
if (ride.departureTime <= new Date()) {
  return Result.fail<Ride>(
    'Departure time must be in the future'
  );
}
```

### DDD/Architecture Alignment
- [ ] **Domain Layer Fix:** [z.B. Invarianten-Validierung in Aggregate hinzufügen]
- [ ] **Application Layer Fix:** [z.B. Use Case anpassen]
- [ ] **Infrastructure Layer Fix:** [z.B. Repository Query korrigieren]
- [ ] **API Fix:** [z.B. OpenAPI Spec aktualisieren, Validation hinzufügen]
- [ ] **Domain Event hinzufügen:** [z.B. FahrtValidierungFehlgeschlagen Event]

### Breaking Changes
- [ ] Keine Breaking Changes
- [ ] API Breaking Change (Versionierung erforderlich)
- [ ] Database Migration erforderlich
- [ ] Frontend Anpassungen erforderlich

---

## Test-Strategie

### Unit Tests
```typescript
// Test für Fix
describe('Ride Aggregate', () => {
  it('should reject ride creation with past departure time', () => {
    const pastDate = new Date('2025-01-01');
    const result = Ride.create({ departureTime: pastDate });
    
    expect(result.isFailure).toBe(true);
    expect(result.error).toContain('must be in the future');
  });
});
```

### Integration Tests
- [ ] Use Case Test aktualisieren
- [ ] Repository Test aktualisieren
- [ ] API Contract Test (Dredd/Pact)

### E2E Tests
- [ ] Reproduktions-Szenario als E2E Test
- [ ] Regression Test hinzufügen

---

## Akzeptanzkriterien

### Funktionale Kriterien
- [ ] Bug ist behoben und nicht mehr reproduzierbar
- [ ] Erwartetes Verhalten wird erreicht
- [ ] Keine neuen Bugs eingeführt (Regression Tests)

### Nicht-Funktionale Kriterien
- [ ] **Performance:** Keine Verschlechterung
- [ ] **Security:** Keine neuen Vulnerabilities
- [ ] **Code Quality:** Fix folgt Best Practices

### DDD/Architecture Kriterien
- [ ] **Invarianten gewahrt:** Aggregate in konsistentem Zustand
- [ ] **Domain Events korrekt:** Events werden publiziert (falls nötig)
- [ ] **Layer Separation:** Fix im richtigen Layer (Domain/Application/Infrastructure)
- [ ] **Ubiquitous Language:** Code nutzt Fachbegriffe korrekt

---

## Definition of Done

- [ ] Code Review durchgeführt
- [ ] Unit Tests für Bug-Fix geschrieben und erfolgreich
- [ ] Integration Tests aktualisiert
- [ ] E2E Test für Reproduktionsszenario hinzugefügt
- [ ] Dokumentation aktualisiert (falls API/Verhalten geändert)
- [ ] Domain Model aktualisiert (falls Struktur geändert)
- [ ] OpenAPI Spec aktualisiert (falls API geändert)
- [ ] CHANGELOG.md aktualisiert (bei Breaking Changes)

---

## Referenzen

- **User Story:** [Link/ID zur betroffenen User Story]
- **Domain Model:** `docs/architecture/domain-models/[context].puml`
- **API Spec:** `docs/api/[context]/[resource].yaml`
- **Architecture Decisions:** `docs/architecture/architecture-decisions.md`
- **DDD Best Practices:** `.agent-resources/best-practices/ddd.best-practices.md`
- **Testing Strategy:** `.agent-resources/definitions-conventions/testing-strategy.md`

---

## Screenshots / Logs

[Falls vorhanden: Screenshots, Videos, ausführliche Logs anhängen]

---

*Hinweis: Bei kritischen Bugs sofort das Team informieren und Hotfix-Branch erstellen.*
