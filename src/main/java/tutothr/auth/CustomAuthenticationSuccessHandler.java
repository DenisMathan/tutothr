package tutothr.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tutothr.auth.config.MyUserDetails;
import tutothr.common.services.MailService;

import java.io.IOException;
import java.util.Random;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MailService mailService;

    CustomAuthenticationSuccessHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String code = String.format("%06d", new Random().nextInt(999999));
        // Prüfen, ob 2FA für diesen User aktiviert ist
        if (userDetails.getDbUser().isTwoFactorEnabled()) {
            // User in eine "Zwischen-Session" speichern oder Flag setzen
            request.getSession().setAttribute("2FA_PENDING", true);
            request.getSession().setAttribute("2FA_USER_ID", userDetails.getId());
            request.getSession().setAttribute("2FA_CODE", code);

            mailService.sendTwoFactorCode(userDetails.getDbUser(), code);

            
            // Weiterleiten zur Code-Eingabe
            getRedirectStrategy().sendRedirect(request, response, "/login/verify-2fa");
        } else {
            // Kein 2FA -> Normal weiter zum Home
            super.setDefaultTargetUrl("/home");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
