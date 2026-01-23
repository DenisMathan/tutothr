# tutothr

## Start Project
- cp application-local.properties.dummy ./src/main/resources/application-local.properties
- refill "your Variable"
- ./mvnw compile
- ./mvnw spring-boot:run

## Variables for localProperties
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

### Paypal
```properties
paypal.client-id=paypal-id
paypal.client-secret=paypal-secret
```

### other
```properties
app.security.remember-me.key=your-key
security.jwt.secret-key=another-key
```


## 📚 Documentation

*   [API Endpoints](docs/API.md) - Übersicht aller REST-Endpunkte und Controller-Pfade.
*   [Security Concept](docs/SECURITY.md) - Details zu Authentifizierung, OAuth2 und Rollen.
*   [Architecture & Relations](docs/ARCHITECTURE.md) - Klassendiagramme und Architektur-Übersicht.
*   [ER-Model](docs/ER-Model.md) - Datanbank-Modell.
