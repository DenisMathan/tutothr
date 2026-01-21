package tutothr.auth.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import tutothr.user.User;
import tutothr.common.utils.enums.RolesEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomOidcUser implements OidcUser, AppPrincipal {
    private final OidcUser delegate;
    private final User dbUser;
    private final Set<GrantedAuthority> authorities;

    public CustomOidcUser(OidcUser delegate, User dbUser) {
        this.delegate = delegate;
        this.dbUser = dbUser;
        this.authorities = new HashSet<>();
        
        // OAuth2 Authorities (Scopes etc.)
        this.authorities.addAll(delegate.getAuthorities());
        
        // DB Roles
        for (RolesEnum role : dbUser.getRoles()) {
            if (role != null) {
                this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
            }
        }
    }

    @Override
    public Long getId() {
        return dbUser.getId();
    }

    @Override
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
        return authorities;
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
