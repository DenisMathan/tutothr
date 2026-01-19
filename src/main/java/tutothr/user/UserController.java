package tutothr.user;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import tutothr.auth.AuthService;
import tutothr.user.interfaces.UserRepositoryI;

import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private UserRepositoryI userRepository;

    public UserController(UserRepositoryI userRepository, UserService userService, AuthService authService) {
        super();
        this.userRepository = userRepository;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/set-username")
    public String getMethodName(Model model) {
        User user = authService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "views/auth/set-username";
    }


    @GetMapping("/user/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        // User user = userRepository.findById(id).orElseThrow();
        User user = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found");
            return "/error/404";
        }
        UserDTO userDTO = userService.mapToDTO(user);
        model.addAttribute("user", userDTO);

        return "views/users/user-profile";
    }

    @GetMapping("/profil")
    public String getMyProfile(Model model) {
        User user = authService.getCurrentUser();
        if (user == null) {
            System.out.println("No authenticated user found");
            return "redirect:/login"; 
        }
        UserDTO userDTO = userService.mapToDTO(user);
        model.addAttribute("user", userDTO);
        return "views/users/user-profile";
    }

    @PutMapping("/admin/user/save/{id}")
    public String saveUser(@ModelAttribute @Valid UserDTO userDTO, BindingResult result, Model model,
            @PathVariable Long id) {
        if (result.hasErrors()) {
            userDTO = userService.handleValidationErrors(userDTO, result.getFieldErrors());
            model.addAttribute("user", userDTO);
            return "/views/courses/course-edit";
        }
        userService.update(userDTO);
        return "redirect:/admin/all";
    }

    @PutMapping("/set-username")
    public String setUsername(@ModelAttribute User user, BindingResult result, Model model,
            Authentication authentication) {
        userService.updateUsername(user, authentication);
        return "redirect:/home";
    }

    @GetMapping(value = { "", "/admin/all" })
    public String showUserList(Model model,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int size) {
        try {
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
