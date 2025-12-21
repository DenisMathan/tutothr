package tutothr.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepositoryI userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof DisabledException) {
            String email = request.getParameter("email");
            Optional<User> user = userRepository.findByEmailIgnoreCase(email);

            if (user.isPresent()) {
                // Redirect zur Verifikations-Info-Seite mit User-ID
                getRedirectStrategy().sendRedirect(request, response, "/verify/" + user.get().getId());
                return;
            }
        }

        // Standard-Verhalten für andere Fehler (z.B. falsches Passwort) -> /login?error
        setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
