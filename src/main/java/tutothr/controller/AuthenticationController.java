package tutothr.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tutothr.repository.UserRepositoryI;
import tutothr.repository.RoleRepositoryI;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;

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
    private final UserRepositoryI userRepository;
    private final RoleRepositoryI roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(UserRepositoryI userRepository,
            RoleRepositoryI roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public boolean alreadyLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("registrationForm") RegistrationForm form,
            BindingResult bindingResult,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (bindingResult.hasErrors())
            return "register";

        if (form.getUsername() == null || form.getUsername().isBlank()) {
            return "redirect:/register?error";
        }

        if (form.getPassword() == null || !form.getPassword().equals(form.getConfirmPassword())) {
            // best practise: add FieldError and return view so errors show in form
            return "redirect:/register?error";
        }

        // username unique prüfen
        if (userRepository.findByLoginIgnoreCase(form.getUsername()).isPresent()) {
            return "redirect:/register?error"; // oder bessere Fehlermeldung
        }

        // Benutzer anlegen
        tutothr.model.User u = new tutothr.model.User();
        u.setLogin(form.getUsername());
        u.setEmail(form.getEmail());
        u.setPassword(passwordEncoder.encode(form.getPassword()));
        u.setActive(true);

        // Rolle holen und zuweisen (z.B. STUDENT)
        roleRepository.findByDescriptionIgnoreCase("STUDENT").ifPresent(r -> {
            u.getRoles().add(r);
        });

        userRepository.save(u);

        // Optional: Auto-Login
        try {
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(form.getUsername(),
                    form.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ex) {
            return "redirect:/login?registered";
        }

        // redirect zur Startseite oder SavedRequest
        return "redirect:/home";
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
