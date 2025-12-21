package tutothr.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import tutothr.auth.config.CustomOidcUser;
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
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getAttribute("email");
        //get or create user
        User _user = userRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
            User user = new User();
            user.setEmail(email);
            user.setActive(true);
            user.setAuthProvider(AuthProvider.GOOGLE); 
            user.setVerified(true);
            roleRepository.findByDescriptionIgnoreCase("STUDENT").ifPresent(role -> {
                user.getRoles().add(role);
            });
            return userRepository.save(user);
        });

        return new CustomOidcUser(oidcUser, _user);
    }
}
