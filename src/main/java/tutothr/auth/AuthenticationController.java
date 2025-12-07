package tutothr.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class RegistrationForm {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

@Controller
public class AuthenticationController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    public boolean alreadyLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("registrationForm") RegisterUserDTO form,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if(!authService.register(form)) {
            return "redirect:/register?error";
        }

        // Optional: Auto-Login
        try {
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(form.getEmail(),form.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println(auth.isAuthenticated());
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return "redirect:/home";
        } catch (Exception ex) {
            System.out.println(ex);
            return "redirect:/login?registered";
        }

    }

    @GetMapping({ "/register" })
    public String showRegister(Authentication authentication, Model model) {
        System.out.println("here in register!");
        if (alreadyLoggedIn(authentication)) {
            // bereits angemeldet -> weiterleiten
            return "redirect:/home";
        }

        model.addAttribute("registrationForm", new RegistrationForm());
        // nicht angemeldet -> Registrierungsseite zeigen
        return "register";
    }

    @GetMapping({ "/login" })
    public String showLogin(Authentication authentication) {
        if (alreadyLoggedIn(authentication)) {
            // bereits angemeldet -> weiterleiten
            return "redirect:/home";
        }
        // nicht angemeldet -> Registrierungsseite zeigen
        return "login";
    }
    

    @GetMapping("/logout")
    public String postMethodName(Authentication auth) {
        // TODO: process POST request
        if (!alreadyLoggedIn(auth))
            return "redirect:/login";

        return "login";
    }

}
