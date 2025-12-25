# Analyse der Backend-Kommunikation mit Keycloak

Diese Dokumentation beschreibt den Datenfluss und die Sicherheitsmechanismen zwischen dem Client (z.B. Flutter App), dem Spring Boot Backend und dem Keycloak Identity Provider.

## Übersicht der Architektur

Die Anwendung folgt einer **Hexagonalen Architektur (Ports & Adapters)**. Das System besteht aus drei Hauptkomponenten:

1.  **Client**: Startet Anfragen (REST).
2.  **Spring Boot Backend**:
    *   Fungiert als **Resource Server** (validiert Tokens).
    *   Fungiert als **Client** gegenüber der Keycloak Admin API (verwaltet User).
3.  **Keycloak**: Identity Provider (IdP) und Authorization Server.

---

## 1. Benutzer-Registrierung (Flow)

**Ziel**: Ein neuer Benutzer registriert sich über den Client. Der User muss sowohl in der lokalen Datenbank (für Geschäftslogik) als auch in Keycloak (für Auth) existieren.

### Ablauf:
1.  **Client**: Sendet `POST /api/v1/users/register` mit Username, Email, Passwort etc.
2.  **UserController** (`UserDomain`):
    *   Nimmt das DTO entgegen.
    *   Delegiert an `UserApplicationService`.
3.  **UserApplicationService**:
    *   Prüft, ob der User lokal bereits existiert.
    *   Speichert den User zunächst in der lokalen PostgreSQL-Datenbank (`UserRepository`).
    *   Ruft `KeycloakUserService.createUser(...)` auf.
4.  **KeycloakUserServiceAdapter** (Infrastructure):
    *   Nutzt den konfigurierten **Keycloak Admin Client** (siehe `KeycloakConfig`).
    *   Greift mit **Service-Account-Credentials** (Client Credentials Grant) auf die Keycloak Admin API zu.
    *   Erstellt den User im `demo-realm`.
    *   Setzt das Passwort und weist Standard-Rollen (z.B. "user") zu.
    *   Gibt die generierte Keycloak-ID (UUID) zurück.
5.  **UserApplicationService**:
    *   Verknüpft den lokalen User mit der Keycloak-ID (`linkWithKeycloak`).
    *   Aktualisiert den User in der DB.

**Wichtig**: Da Sie begrenzte Spring-Kenntnisse erwähnten – hier ist keine "Magie" am Werk. Der Adapter benutzt einfach eine Java-Bibliothek (`keycloak-admin-client`), die HTTP-Requests an Keycloak sendet, genau wie Sie es manuell mit Postman oder curl tun würden.

---

## 2. Benutzer-Login (Flow)

**Ziel**: Der Benutzer tauscht seine Credentials gegen ein Access Token.

### Ablauf:
1.  **Client**: Sendet `POST /api/v1/users/login` mit Username und Passwort.
    *   *Hinweis*: Dies ist ein "Proxy-Login". Normalerweise würde der Client direkt mit Keycloak reden (Recommended Flow). Hier leitet das Backend die Anfrage weiter.
2.  **UserController**:
    *   Delegiert an `UserApplicationService`.
3.  **KeycloakUserServiceAdapter**:
    *   Öffnet eine neue Keycloak-Session.
    *   Sendet die Credentials (`grant_type=password`) an den Keycloak Token Endpoint.
    *   Erhält `AccessTokenResponse` (JWT, Refresh Token, Expiry).
4.  **Backend**: Gibt das Token-Objekt an den Client zurück.

---

## 3. Authentifizierte Anfragen (Flow)

**Ziel**: Der Client möchte auf geschützte Ressourcen zugreifen (z.B. `GET /api/v1/users/me`).

### Ablauf:
1.  **Client**: Sendet Request mit Header `Authorization: Bearer <jwt_token>`.
2.  **Spring Security (`SecurityConfig`)**:
    *   Fängt den Request ab ("Filter Chain").
    *   Ist als **OAuth2 Resource Server** konfiguriert.
    *   **JWT-Validierung**:
        *   Prüft die Signatur des Tokens anhand der Public Keys von Keycloak (geladen via `jwk-set-uri` aus `application.yml`).
        *   Prüft `issuer-uri` (wurde das Token von *diesem* Keycloak ausgestellt?).
        *   Prüft Ablaufdatum (exp).
    *   **Converter**: Extrahiert Rollen aus dem JWT (`realm_access.roles`) und wandelt sie in Spring-Authorities um (`ROLE_user`).
3.  **UserController**:
    *   Wenn das Token gültig ist, wird die Methode ausgeführt.
    *   Via `SecurityContextHolder.getContext().getAuthentication()` kann auf die User-Infos im Token (z.B. `preferred_username`) zugegriffen werden.

---

## Konfigurations-Checkliste

Diese Dateien steuern das Verhalten:

| Datei | Zweck | Wichtige Properties |
| :--- | :--- | :--- |
| `application.yml` | Hauptkonfiguration | `spring.security.oauth2.resourceserver.jwt.*`<br>`keycloak.*` (Admin-Zugriff) |
| `SecurityConfig.java` | Sicherheitsregeln | `.authorizeHttpRequests(...)`<br>`.oauth2ResourceServer(...)` |
| `KeycloakConfig.java` | Admin-Client Setup | Erstellt den `Keycloak` Bean für API-Aufrufe. |

## Zusammenfassung für Java-Entwickler

*   **Rest Controller**: Ganz normale Java-Klassen mit Annotations (`@RestController`).
*   **Kommunikation**: Läuft fast ausschließlich über HTTP/REST.
    *   *Client -> Backend*: REST
    *   *Backend -> Keycloak*: REST (gekapselt in der Admin-Client Library)
*   **Security**: Spring Security nimmt Ihnen die Arbeit ab, jeden Request manuell zu parsen. Es prüft das JWT automatisch im Filter, bevor Ihr Controller überhaupt aufgerufen wird.

