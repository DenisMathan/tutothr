# tutothr

## Start Project
- cp .env.dummy .env
- refill variables
- export $(grep -v '^#' .env | xargs)
- click play in your IDE

## Environment Variables
### MailService
```properties
MAIL=mailAddresse
MAILPW=passwort
```
### Oauth2
Für OAuth2 müssen folgende Variablen in der `.env` gesetzt sein:
```properties
GOOGLE_CLIENT_ID=deine-client-id
GOOGLE_CLIENT_SECRET=dein-client-secret
```

## 📚 Documentation

*   [API Endpoints](docs/API.md) - Übersicht aller REST-Endpunkte und Controller-Pfade.
*   [Security Concept](docs/SECURITY.md) - Details zu Authentifizierung, OAuth2 und Rollen.
*   [Architecture & Relations](docs/ARCHITECTURE.md) - Klassendiagramme und Architektur-Übersicht.
