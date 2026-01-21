package tutothr.auth.config;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import tutothr.user.User;

public interface AppPrincipal {
    Long getId();
    User getDbUser();
    Collection<? extends GrantedAuthority> getAuthorities();
}
