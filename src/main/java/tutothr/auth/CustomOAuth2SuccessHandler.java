package tutothr.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepositoryI userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }
        if (user != null && (user.getUsername() == null || user.getUsername().isBlank())) {
            response.sendRedirect("/set-username");
        } else {
            response.sendRedirect("/home");
        }
    }
}
