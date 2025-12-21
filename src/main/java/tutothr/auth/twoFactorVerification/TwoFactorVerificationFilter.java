package tutothr.auth.twoFactorVerification;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TwoFactorVerificationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Prüfen, ob in der Session das Flag gesetzt ist
        Boolean is2faPending = (Boolean) request.getSession().getAttribute("2FA_PENDING");
        String requestURI = request.getRequestURI();

        // Wenn 2FA aussteht...
        if (is2faPending != null && is2faPending) {
            // ...aber der User nicht gerade auf der 2FA-Seite ist oder statische Ressourcen lädt...
            if (!requestURI.startsWith("/login/verify-2fa") && 
                !requestURI.startsWith("/css") && 
                !requestURI.startsWith("/webjars")) {
                
                // ...dann zwingen wir ihn zurück zur Eingabe!
                response.sendRedirect("/login/verify-2fa");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
