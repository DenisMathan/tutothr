package tutothr.user;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import tutothr.auth.CustomOidcUser;
import tutothr.user.interfaces.UserRepositoryI;

import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class UserController {

    private final UserService userService;
    private UserRepositoryI userRepository;

    public UserController(UserRepositoryI userRepository, UserService userService) {
        super();
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/set-username")
    public String getMethodName(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null)
            return "redirect:/login"; // Sicherheitsnetz
        String email = principal.getAttribute("email");
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        model.addAttribute("user", user);
        return "views/auth/set-username";
    }

    @PutMapping("/set-username")
    public String setUsername(@ModelAttribute User user, BindingResult result, Model model, Authentication authentication) {
        userService.updateUsername(user, authentication);
        return "redirect:/home";
        // TODO: process POST request

    }

    @GetMapping(value = { "", "/admin/all" })
    public String showUserList(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size) {
        try {

            // simple path: list all users instead of paginated students
            List<User> users = userRepository.findAll();
            model.addAttribute("keyword", keyword);
            model.addAttribute("users", users);
            // no pagination in this simple view
            model.addAttribute("entitytype", "user");
            model.addAttribute("totalItems", users.size());
            model.addAttribute("pageSize", users.size());
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "/views/users/user-all";
    }
}
