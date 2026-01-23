# Security Concept

## 🛡 Overview
Das Projekt setzt auf **Spring Security 6** und implementiert eine **hybride Authentifizierungsstrategie**, die klassische Logins mit modernen OAuth2/OIDC-Verfahren kombiniert.

*   **Framework:** Spring Security 6.x
*   **Standards:** OAuth2, OIDC, BCrypt
*   **Session:** Stateful (JSESSIONID)

---

## SecurityChain

Abbildunng der Filter-Chains aus `SecurityConfig.java`. Es werden zwei separate Chains konfiguriert: eine für die API (Stateless, JWT) und eine für die Web-Applikation (Stateful, Session).

```mermaid
graph TD
    Request([User Request]) --> Matcher{URL Pattern?}

    %% API Chain Configuration
    Matcher -->|"/api/**"| ApiStart
    subgraph ApiChainScope ["API Security Chain (Stateless)"]
        direction TB
        ApiStart[Start] --> JwtFilter["JwtAuthenticationFilter<br>Validierung des Bearer Tokens"]
        JwtFilter --> ApiAuthZ["AuthorizationFilter<br>Check permissions"]
    end

    %% Web Chain Configuration
    Matcher -->|"/**"| WebStart
    subgraph WebChainScope ["Web Security Chain (Stateful / Session)"]
        direction TB
        WebStart[Start] --> Context["SecurityContextHolderFilter<br>Session Restoration"]
        Context --> TwoFactor["TwoFactorVerificationFilter<br>Pre-Auth Check"]
        
        TwoFactor --> AuthMech
        
        subgraph AuthMech ["Authentication Mechanisms"]
            Form["UsernamePasswordAuthenticationFilter<br>Form Login"]
            OAuth["OAuth2LoginAuthenticationFilter<br>Google Login"]
            RememberMe[RememberMeAuthenticationFilter]
        end
        
        AuthMech --> Anon[AnonymousAuthenticationFilter]
        Anon --> RedirCheck["RedirectCheckFilter<br>Check Username / Onboarding"]
        
        RedirCheck -->|Incomplete Profile| SetUser([Redirect /set-username])
        RedirCheck -->|Complete| WebAuthZ["AuthorizationFilter<br>RBAC / Access Control"]
    end
    
    %% Endpoints
    ApiAuthZ -->|Allowed| ApiController([API Endpoints])
    WebAuthZ -->|Allowed| WebController([Web Controller])
    
    %% Error Handling
    ApiAuthZ -->|Denied| ErrorNode([401/403 Error])
    WebAuthZ -->|Denied| LoginRedirect([Redirect Login / Error])

    %% Styling
    classDef filter fill:#e1f5fe,stroke:#01579b,stroke-width:1px;
    classDef decision fill:#fff3e0,stroke:#ef6c00,stroke-width:1px;
    class JwtFilter,TwoFactor,Form,OAuth,RememberMe,Anon,RedirCheck,ApiAuthZ,WebAuthZ,Context filter;
    class Matcher decision;
```

## 🔐 Authentication (AuthN)

Wir unterstützen zwei parallele Wege, um die Identität eines Nutzers festzustellen.

### 1. Local Auth (Form Login)
*   **Credential Storage:** E-Mail & BCrypt-gehashtes Passwort in der Datenbank.
*   **Provider:** `DaoAuthenticationProvider` via `AuthService`.
*   **Security Measure:** User mit `AuthProvider.GOOGLE` können sich **nicht** per Passwort einloggen (Schutz vor Empty-Password-Attacks).

### 2. Social Auth (Google OIDC)
*   **Provider:** Google Identity Platform.
*   **Flow:** Authorization Code Flow.
*   **Mapping:** Google-User werden beim ersten Login automatisch in der lokalen DB angelegt (`CustomOidcUserService`).

### 🏗 Unified Principal Architecture
Damit die Applikation nicht wissen muss, woher der User kommt, werden beide Login-Arten auf ein gemeinsames Interface gemappt.

| Login Type | Implementation | Interface |
| :--- | :--- | :--- |
| Form Login | `MyUserDetails` | `AppPrincipal` |
| Google Login | `CustomOidcUser` | `AppPrincipal` |

**Vorteil:** Im Code kann immer `((AppPrincipal) authentication.getPrincipal()).getDbUser()` aufgerufen werden.

---

## 🚦 Authorization (AuthZ)

Der Zugriff wird über **Role-Based Access Control (RBAC)** gesteuert.

### Role Hierarchy
| Rolle | Beschreibung | Zugriff |
| :--- | :--- | :--- |
| `ROLE_ADMIN` | Systemadministrator | Voller Zugriff auf `/admin/**` (Kategorien, User-Verwaltung) |
| `ROLE_TUTOR` | Lehrender | Zugriff auf `/tutor/**` (Kurse erstellen, Eigene Kurse Bearbeiten, Eigene Kapitel verwalten) |
| `ROLE_STUDENT` | Lernender | Zugriff auf  (Kurse ansehen, Profil) |

### Public Endpoints
Folgende Pfade sind ohne Login erreichbar (Whitelist):
*   `/login`, `/register`
*   Static Resources (`/css/**`, `/js/**`, `/images/**`)

---

## 🔄 Authentication Flow

Der folgende Ablauf zeigt, wie ein Request verarbeitet wird und wie der **Onboarding-Zwang** (Username setzen) technisch umgesetzt ist.

```mermaid
flowchart TD
    Start([User Request]) --> AuthCheck{Authenticated?}

    %% Not Authenticated Flow
    AuthCheck -->|No| LoginPage[Login Page]
    LoginPage -->|Form Login| AuthService["AuthService<br/>(DB Check)"]
    LoginPage -->|Google OAuth| OidcService["CustomOidcUserService<br/>(Google Check)"]

    AuthService -->|Success| PrincipalA[MyUserDetails]
    OidcService -->|Success| PrincipalB[CustomOidcUser]

    %% Unified Flow
    PrincipalA --> UnifiedPrincipal
    PrincipalB --> UnifiedPrincipal

    UnifiedPrincipal --> UsernameFilter{Username set?}

    %% Username Check Filter
    UsernameFilter -->|No| SetUsername["Redirect: /set-username"]
    SetUsername -->|Input| UpdateUser["Update DB & Context"]
    UpdateUser --> UsernameFilter

    %% Authorization
    UsernameFilter -->|Yes| RoleCheck{Has Required Role?}
    
    PermissionService
    RoleCheck -->|No| Forbidden[403 Forbidden]
    RoleCheck -->|Yes| PermissionService{PermissionService <br>isOwner?}
    PermissionService -->|yes| AccessR([Access Granted<br>with Readrights])
    PermissionService -->|no| AccessCRUD([Access Granted<br> with CRUD-Rights])

    %% Styling
    classDef decision fill:#fff3e0,stroke:#f57c00,stroke-width:2px;
    classDef process fill:#e3f2fd,stroke:#1565c0,stroke-width:2px;
    classDef success fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
    classDef error fill:#ffebee,stroke:#c62828,stroke-width:2px;

    class AuthCheck,UsernameFilter,RoleCheck decision;
    class LoginPage,AuthService,OidcService,PrincipalA,PrincipalB,UnifiedPrincipal,UpdateUser,SetUsername process;
    class Access success;
    class Forbidden error;
```

---

## 🧩 Key Components

| Komponente | Beschreibung |
| :--- | :--- |
| `SecurityConfig` | Konfiguriert die `SecurityFilterChain`, CSRF, und Public Endpoints. |
| `UsernameCheckFilter` | Ein `OncePerRequestFilter`, der nach dem Login prüft, ob der User einen Usernamen hat. Falls nicht -> Redirect. |
| `CustomOidcUserService` | Erweitert den Standard-OIDC-Service, um Google-User in die eigene DB zu synchronisieren. |
| `AuthProvider` | Enum (`LOCAL`, `GOOGLE`) zur Unterscheidung der Login-Quelle im `User`-Entity. |
