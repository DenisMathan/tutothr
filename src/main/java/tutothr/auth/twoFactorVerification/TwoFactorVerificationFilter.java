package tutothr.auth.twoFactorVerification;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TwoFactorVerificationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // WICHTIG: WebSocket-Endpoint (/ws) muss ignoriert werden!
        // Sonst bricht der Handshake wegen Session-Checks oder Redirects ab.
        if (path.startsWith("/ws")) {
            return true;
        }

        // Auch statische Ressourcen explizit ignorieren (Performance)
        if (path.startsWith("/css") ||
                path.startsWith("/js") ||
                path.startsWith("/webjars") ||
                path.startsWith("/images")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Kleine Verbesserung: getSession(false) verhindert, dass versehentlich
        // eine neue Session erstellt wird, wenn noch keine existiert.
        var session = request.getSession(false);

        if (session != null) {
            Boolean is2faPending = (Boolean) session.getAttribute("2FA_PENDING");
            String requestURI = request.getRequestURI();

            // Wenn 2FA aussteht...
            if (Boolean.TRUE.equals(is2faPending)) {
                // ...aber der User nicht gerade auf der 2FA-Seite ist...
                if (!requestURI.startsWith("/login/verify-2fa")) {

                    // ...dann zwingen wir ihn zurück zur Eingabe!
                    response.sendRedirect("/login/verify-2fa");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}