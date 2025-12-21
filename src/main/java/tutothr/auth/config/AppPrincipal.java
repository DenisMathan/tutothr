package tutothr.auth.config;

import tutothr.user.User;

public interface AppPrincipal {
    Long getId();
    User getDbUser();
}
