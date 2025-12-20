package tutothr.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import tutothr.role.RoleRepositoryI;
import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepositoryI userRepository;

    @Autowired
    private RoleRepositoryI roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegiere an den Standard-OidcUserService, um den User zu laden
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");

        // Prüfen, ob User existiert, sonst anlegen
        User _user = userRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setActive(true);
            // Username ist bei Google oft der Name, kann aber auch null sein
            // if (oidcUser.getAttribute("name") != null) {
            //     user.setUsername(oidcUser.getAttribute("name"));
            // }
            
            roleRepository.findByDescriptionIgnoreCase("STUDENT").ifPresent(role -> {
                user.getRoles().add(role);
            });
            return userRepository.save(user);
        });

        return new CustomOidcUser(oidcUser, _user);
    }
}
