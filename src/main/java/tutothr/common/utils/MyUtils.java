package tutothr.common.utils;

import org.springframework.security.core.Authentication;

import tutothr.auth.config.CustomOidcUser;
import tutothr.user.User;

public class MyUtils {
    private MyUtils() {} // private constructor to prevent instantiation

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isGoogleLogin(Authentication auth) {
        return auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomOidcUser;
    }

    public static boolean missingUsername(Authentication auth) {
        CustomOidcUser customUser = (CustomOidcUser) auth.getPrincipal();
        User dbUser = customUser.getDbUser();
        return (dbUser != null && (dbUser.getUsername() == null || dbUser.getUsername().isBlank()));
    }

}
