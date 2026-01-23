package tutothr.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import tutothr.auth.dtos.LoginUserDTO;
import tutothr.auth.dtos.RegisterUserDTO;
import tutothr.user.User;

@Controller
public class AuthenticationController {
    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;

    }

    public boolean alreadyLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute("registrationForm") @Valid RegisterUserDTO form,
            BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {

        if (!authService.confirmPasswords(form)) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword",
                    "Die Passwörter stimmen nicht überein.");
        }

        if (bindingResult.hasErrors()) {
            // Transfer errors to DTO so form.html can display them
            bindingResult.getFieldErrors()
                    .forEach(error -> form.addValidationError(error.getField(), error.getDefaultMessage()));
            return "views/auth/register";
        }

        User user = authService.register(form);

        if (user == null) {
            form.getValidationErrors().forEach((field, msg) -> bindingResult.rejectValue(field, "error." + field, msg));
            return "views/auth/register";
        }
        return "redirect:/verify/" + user.getId();
    }

    @GetMapping({ "/register" })
    public String showRegister(Authentication authentication, Model model) {
        if (alreadyLoggedIn(authentication)) {
            // bereits angemeldet -> weiterleiten
            return "redirect:/home";
        }
        RegisterUserDTO form = new RegisterUserDTO();
        form.setPassword("Password123"); // Vorausgefüllt für einfacheres Testen
        form.setConfirmPassword("Password123");
        model.addAttribute("registrationForm", form);

        // nicht angemeldet -> Registrierungsseite zeigen
        return "views/auth/register";
    }

    @GetMapping({ "/login" })
    public String showLogin(Authentication authentication, Model model) {
        if (alreadyLoggedIn(authentication)) {
            // bereits angemeldet -> weiterleiten
            return "redirect:/home";
        }
        LoginUserDTO form = new LoginUserDTO();
        form.setPassword("Password123"); // Vorausgefüllt für einfacheres Testen
        model.addAttribute("loginForm", form);
        return "views/auth/login";
    }

    @GetMapping("/logout")
    public String postMethodName(Authentication auth) {
        if (!alreadyLoggedIn(auth))
            return "redirect:/login";

        return "views/auth/login";
    }
}
