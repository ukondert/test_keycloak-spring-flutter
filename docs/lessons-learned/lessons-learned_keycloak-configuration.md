# Lessons Learned: Keycloak & Spring Boot Integration

Dieses Dokument beschreibt die Herausforderungen und Lösungen bei der Konfiguration von Keycloak (Version 23) in Verbindung mit einem Spring Boot Backend und einem Flutter Web Frontend.

## 1. CORS (Cross-Origin Resource Sharing)
### Problem
Die Flutter-App (Web) meldete Netzwerkfehler: `The connection errored: The XMLHttpRequest onError callback was canceled.` 
Im Browser-Log (F12) war ein "CORS Policy" Fehler sichtbar.

### Ursache
Das Spring Boot Backend erlaubte nur Anfragen von spezifischen Ports (z. B. 3000 oder 8081). Da Flutter Web zur Laufzeit oft zufällige Ports nutzt, wurde die Anfrage blockiert.

### Lösung
In der `SecurityConfig.java` wurde die CORS-Konfiguration auf `setAllowedOriginPatterns` umgestellt, um alle lokalen Entwicklungs-Ports abzudecken:
```java
configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
```

---

## 2. Keycloak Client-Typ & "Bearer-only" (v23)
### Problem
Das Backend erhielt einen `400 Bad Request` von Keycloak. Eine manuelle Analyse ergab die Fehlermeldung: `{"error":"invalid_client","error_description":"Bearer-only not allowed"}`. In der UI fehlte zudem der Bereich "Capability config".

### Ursache
In Keycloak 23 ist der Modus "Bearer-only" veraltet. Wenn ein Client (wie das Backend) jedoch intern in der Datenbank noch als solcher markiert ist, verweigert Keycloak die Anforderung von Service-Account-Tokens (`client_credentials`).

**Besonderer Fallstrick:** In der `realm-export.json` war für den Client `"bearerOnly": true` hinterlegt. Da Keycloak 23 für solche Clients den Bereich "Capability config" in der UI komplett ausblendet, lässt sich dieser Wert **nicht über die Benutzeroberfläche auf `false` ändern**.

### Lösung
Der Wert muss in der `realm-export.json` manuell auf **`false`** geändert werden, bevor der Realm importiert wird. Zudem muss der Client als **Confidential** konfiguriert sein:
*   **Client authentication**: `ON`
*   **Authentication flow**: `Service accounts roles` aktiviert.

---

## 3. Keycloak Import-Verhalten & Volumes
### Problem
Änderungen in der `realm-export.json` wurden beim Neustart von Docker-Compose ignoriert. Keycloak loggte: `Realm 'demo-realm' already exists. Import skipped`.

### Ursache
Keycloak importiert eine Realm-Datei nur dann, wenn der Realm noch nicht in der Datenbank existiert. Ein einfacher Neustart reicht nicht aus, um geänderte Konfigurationen einzuspielen.

### Lösung
Das Docker-Volume muss gelöscht werden, damit Keycloak mit einer leeren Datenbank startet und den Import erneut durchführt:
```powershell
docker-compose down -v
docker-compose up -d
```
*Hinweis: Dies löscht alle bestehenden Daten in der Datenbank.*

---

## 4. Datenbank-Tabellen nach Volume-Reset
### Problem
Nach dem Zurücksetzen der Docker-Volumes meldete das Backend: `relation "demo_users_schema.users" does not exist`.

### Ursache
Durch das Löschen der Volumes waren auch die Tabellen verschwunden. Da Flyway nur beim Starten der Anwendung die Migrationen prüft, wurden die Tabellen nicht automatisch angelegt, solange das Backend weiterlief.

### Lösung
Das Spring Boot Backend muss nach einem Datenbank-Reset neu gestartet werden, damit Flyway die Tabellen (z. B. die `users`-Tabelle) wieder anlegt.

---

## 5. Berechtigungen (403 Forbidden)
### Problem
Obwohl der Token-Abruf funktionierte, lieferte Keycloak beim Versuch, einen Benutzer zu erstellen, einen `403 Forbidden` oder `unknown_error`.

### Ursache
Der Service-Account des Backend-Clients hatte zwar einen gültigen Token, besaß aber keine Rechte, um Änderungen am Realm (wie Benutzererstellung) vorzunehmen.

### Lösung
Dem Service-Account des Clients muss die technische Rolle (*Service-Account-Role*) **`manage-users`** zugewiesen werden:
1.  Client `demo-backend` auswählen.
2.  Tab **Service accounts roles**.
3.  **Assign role** klicken.
4.  Filter auf **Filter by client** stellen.
5.  Client **`realm-management`** auswählen.
6.  Rolle **`manage-users`** zuweisen.

---

## Checkliste für neue Setups
- [ ] Backend: CORS für localhost-Wildcard erlaubt?
- [ ] Keycloak Client: `Client authentication` auf ON?
- [ ] Keycloak Client: `Service accounts roles` aktiviert?
- [ ] Berechtigungen: `manage-users` aus `realm-management` zugewiesen?
- [ ] Datenbank: Flyway-Migrationen nach Volume-Reset erfolgreich durchgelaufen?
