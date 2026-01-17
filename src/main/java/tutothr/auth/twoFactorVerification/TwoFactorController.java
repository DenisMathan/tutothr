package tutothr.auth.twoFactorVerification;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TwoFactorController {

    @GetMapping("/login/verify-2fa")
    public String verify2faPage() {
        return "views/auth/verify-2fa"; // Deine HTML Seite mit dem Eingabefeld
    }

    @PostMapping("/login/verify-2fa-check")
    public String verify2faCode(@RequestParam("code") String inputCode, HttpServletRequest request) {
        
        // 1. Echten Code aus der Session holen
        String sessionCode = (String) request.getSession().getAttribute("2FA_CODE");
        
        // 2. Vergleichen (Null-Check wichtig, falls Session abgelaufen)
        if (sessionCode != null && sessionCode.equals(inputCode)) {
            
            // 3. Aufräumen & Freigeben
            request.getSession().removeAttribute("2FA_PENDING");
            request.getSession().removeAttribute("2FA_USER_ID");
            request.getSession().removeAttribute("2FA_CODE"); // Code löschen
            
            return "redirect:/home";
        } else {
            return "redirect:/login/verify-2fa?error";
        }
    }
}
