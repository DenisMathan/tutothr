package tutothr.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import tutothr.user.User;

import java.util.Collection;
import java.util.Map;

public class CustomOidcUser implements OidcUser {
    private final OidcUser delegate;
    private final User dbUser;

    public CustomOidcUser(OidcUser delegate, User dbUser) {
        this.delegate = delegate;
        this.dbUser = dbUser;
    }

    public User getDbUser() {
        return dbUser;
    }

    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Hier könnten wir später auch DB-Rollen mit OAuth-Rollen mergen
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        // Das ist der Trick: Wir geben den Username aus der DB zurück!
        if (dbUser.getUsername() != null && !dbUser.getUsername().isBlank()) {
            return dbUser.getUsername();
        }
        return delegate.getName();
    }
}
