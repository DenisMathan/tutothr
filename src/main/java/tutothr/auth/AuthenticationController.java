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
import jakarta.validation.Valid;
import tutothr.auth.dtos.LoginUserDTO;
import tutothr.auth.dtos.RegisterUserDTO;

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
    public String registerNewUser(@ModelAttribute("registrationForm") @Valid RegisterUserDTO form, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {

        if (!authService.confirmPasswords(form)) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Die Passwörter stimmen nicht überein.");
        }

        if (bindingResult.hasErrors()) {
            // Transfer errors to DTO so form.html can display them
            bindingResult.getFieldErrors().forEach(error -> 
                form.addValidationError(error.getField(), error.getDefaultMessage())
            );
            return "/views/auth/register";
        }
        if(!authService.register(form)) {
            return "/views/auth/register";
        }

        try {
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(form.getEmail(),form.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return "redirect:/home";
        } catch (Exception ex) {
            System.out.println(ex);
            return "redirect:/login?registered";
        }
    }

    @GetMapping({ "/register" })
    public String showRegister(Authentication authentication, Model model) {
        if (alreadyLoggedIn(authentication)) {
            // bereits angemeldet -> weiterleiten
            return "redirect:/views/home";
        }
        RegisterUserDTO form = new RegisterUserDTO();
        //TODO remove test data
        form.setEmail("thomi@web.de");
        form.setUsername("Thomi");
        form.setPassword("Password123");
        form.setConfirmPassword("Password123");

        model.addAttribute("registrationForm", form);
        // nicht angemeldet -> Registrierungsseite zeigen
        return "/views/auth/register";
    }

    @GetMapping({ "/login" })
    public String showLogin(Authentication authentication, Model model) {
        if (alreadyLoggedIn(authentication)) {
            // bereits angemeldet -> weiterleiten
            return "redirect:/views/home";
        }
        LoginUserDTO form = new LoginUserDTO();
        //TODO remove test data
        form.setEmail("Denis@email");
        form.setPassword("Password");
        model.addAttribute("loginForm", form);
        // nicht angemeldet -> Registrierungsseite zeigen
        return "/views/auth/login";
    }
    
    @GetMapping("/logout")
    public String postMethodName(Authentication auth) {
        if (!alreadyLoggedIn(auth))
            return "redirect:/views/login";

        return "/views/auth/login";
    }
}
