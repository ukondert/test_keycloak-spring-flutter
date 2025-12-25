# Keycloak Admin REST API - User Registration & Login Test

Dieses Dokument beschreibt, wie man manuell über `curl` einen Benutzer im Keycloak `demo-realm` anlegt und sich anschließend mit diesem Benutzer anmeldet.

## 1. Admin-Token abrufen

Um Benutzer über die Admin-API anlegen zu können, benötigen wir zuerst ein Access-Token des Admin-Benutzers.

### PowerShell
```powershell
$TOKEN_RESPONSE = curl -X POST http://localhost:8080/realms/master/protocol/openid-connect/token `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "username=admin" `
  -d "password=admin" `
  -d "grant_type=password" `
  -d "client_id=admin-cli"

$ADMIN_TOKEN = ($TOKEN_RESPONSE | ConvertFrom-Json).access_token
echo "Admin Token: $ADMIN_TOKEN"
```

### Bash
```bash
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/realms/master/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" | jq -r '.access_token')

echo "Admin Token: $ADMIN_TOKEN"
```

---

## 2. Benutzer registrieren

Mit dem Admin-Token können wir nun einen neuen Benutzer im `demo-realm` anlegen.

### PowerShell
```powershell
curl -i -X POST http://localhost:8080/admin/realms/demo-realm/users `
  -H "Authorization: Bearer $ADMIN_TOKEN" `
  -H "Content-Type: application/json" `
  -d '{
    "username": "curltestuser",
    "email": "curltest@example.com",
    "enabled": true,
    "firstName": "Curl",
    "lastName": "Test",
    "credentials": [
        {
            "type": "password",
            "value": "Password123!",
            "temporary": false
        }
    ]
  }'
```

### Bash
```bash
curl -i -X POST http://localhost:8080/admin/realms/demo-realm/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "curltestuser",
    "email": "curltest@example.com",
    "enabled": true,
    "firstName": "Curl",
    "lastName": "Test",
    "credentials": [
        {
            "type": "password",
            "value": "Password123!",
            "temporary": false
        }
    ]
  }'
```

---

## 3. Login mit dem neuen Benutzer

Nachdem der Benutzer angelegt wurde, testen wir den Login (Token-Abruf) über den Client `demo-flutter-app`.

### PowerShell
```powershell
curl -X POST http://localhost:8080/realms/demo-realm/protocol/openid-connect/token `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "client_id=demo-flutter-app" `
  -d "username=curltestuser" `
  -d "password=Password123!" `
  -d "grant_type=password"
```

### Bash
```bash
curl -X POST http://localhost:8080/realms/demo-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=demo-flutter-app" \
  -d "username=curltestuser" \
  -d "password=Password123!" \
  -d "grant_type=password"
```

## Aufruf über Spring Boot Backend

### Test-Ergebnis:
Der Login-Aufruf funktioniert jetzt einwandfrei:

```powershell
curl -X POST http://localhost:8090/api/v1/users/login `
  -H "Content-Type: application/json" `
  -d '{
    "username": "curltestuser",
    "password": "Password123!"
  }'
```

**Antwort vom Server (gekürzt):**

`{"access_token":"eyJhbG...","token_type":"Bearer", ...}`