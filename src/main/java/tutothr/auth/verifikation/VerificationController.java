package tutothr.auth.verifikation;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import tutothr.auth.config.AppPrincipal;
import tutothr.auth.config.MyUserDetails;
import tutothr.user.User;
import tutothr.user.UserService;

@Controller
public class VerificationController {
    private VerificationService verificationService;

    public VerificationController(VerificationService verificationService, UserService userService,
            AuthenticationManager authenticationManager) {
        this.verificationService = verificationService;

    }

    @GetMapping("/verify/{userId}")
    public String getVerifyView(@PathVariable(required = true) Long userId, Model model) {
        User user = verificationService.getUserById(userId);

        if (user.isVerified()) {
            return "redirect:/login";
        }
        model.addAttribute("info", Map.of(
                "message", "Wir haben dir bereits eine EmailVerifikation an " + user.getEmail() + " gesendet.",
                "userId", user.getId(),
                "resendUrl", verificationService.userTokenExpired(userId)));
        return "/views/auth/verify";
    }

    @GetMapping("/verify/token/{tokenId}")
    public String verifyToken(@PathVariable String tokenId, HttpServletRequest request) {
        User user = verificationService.verifyToken(tokenId);
        if (user != null) {
            try {
                AppPrincipal principal = new MyUserDetails(user);

                // 1. Authentication direkt erstellen (OHNE AuthenticationManager)
                // Der Konstruktor mit 3 Argumenten setzt automatisch authenticated=true
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        Collections.emptyList() // Leere Liste statt null, um NullPointerExceptions zu vermeiden
                );
                // 2. SecurityContext setzen
                SecurityContextHolder.getContext().setAuthentication(auth);
                // 3. Session aktualisieren (damit der Login bestehen bleibt)
                request.getSession().setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());
                return "redirect:/home";
            } catch (Exception ex) {
                return "redirect:/login?registered";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/verify/resend-mail")
    public String resendVerificationEmail(@RequestParam Long userId) {
        verificationService.sendVerificationEmail(userId);

        return "redirect:/verify/" + userId;
    }

}
