package tutothr.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import tutothr.auth.AuthService;

import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        super();
        this.userService = userService;
        this.authService = authService;
    }

    @DeleteMapping("/user/delete/self")
    public String deleteSelf(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = authService.getCurrentUser();
        if (user != null) {
            userService.delete(user);
            authService.logout(request, response, authentication);
        }

        return "redirect:/login?deleted";
    }
    @DeleteMapping("/user/delete/{id}")
    public String deleteUserById(@PathVariable Long id) {
        User currentUser = authService.getCurrentUser();
        boolean isAdmin = currentUser != null
                && currentUser.getRoles().contains(tutothr.common.utils.enums.RolesEnum.ADMIN);
        User user = userService.getUserById(id);
        if (isAdmin && user != null) {
            userService.delete(user);
        }
        return "redirect:/admin/all?deleted";
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
        User currentUser = authService.getCurrentUser();
        User user = userService.getUserById(id);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found");
            return "error/404";
        }
        UserDTO userDTO = userService.mapToDTO(user);

        // Check if current user is admin
        boolean isAdmin = currentUser != null
                && currentUser.getRoles().contains(tutothr.common.utils.enums.RolesEnum.ADMIN);

        if (isAdmin) {
            userDTO.setAdminFields();
        }

        model.addAttribute("user", userDTO);
        model.addAttribute("isOwnProfile", currentUser != null && currentUser.getId().equals(id));

        return "views/users/user-profile";
    }

    @GetMapping("users/profil")
    public String getMyProfile(Model model) {
        User user = authService.getCurrentUser();
        if (user == null) {
            System.out.println("No authenticated user found");
            return "redirect:/login";
        }
        UserDTO userDTO = userService.mapToDTO(user);
        // Check if current user is admin
        boolean isAdmin = user.getRoles().contains(tutothr.common.utils.enums.RolesEnum.ADMIN);
        if (isAdmin) {
            userDTO.setAdminFields();
        }
        model.addAttribute("user", userDTO);
        model.addAttribute("isOwnProfile", true);
        return "views/users/user-profile";
    }

    @PutMapping("/user/save/{id}")
    public String saveUser(@ModelAttribute @Valid UserDTO userDTO, BindingResult result, Model model,
                           @PathVariable Long id, Authentication authentication) {

        User currentUser = authService.getCurrentUser();
        boolean isAdmin = currentUser != null
                && currentUser.getRoles().contains(tutothr.common.utils.enums.RolesEnum.ADMIN);

        // Security Check 1: Normal users can only update themselves
        if (!isAdmin && (currentUser == null || !currentUser.getId().equals(id))) {
            return "redirect:/error/403";
        }

        // Security Check 2: Normal users cannot update sensitive fields
        if (!isAdmin) {
            userDTO.clearAdminFields();
        }

        if (result.hasErrors()) {
            userDTO = userService.handleValidationErrors(userDTO, result.getFieldErrors());
            model.addAttribute("user", userDTO);
            return "views/users/user-profile";
        }
        userService.update(userDTO);

        if (isAdmin) {
            return "redirect:/user/" + id;
        }
        return "redirect:/users/profil";
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
                               @RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "5") int size,
                               @RequestParam(required = false) String filter) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            String search = (keyword != null && !keyword.isEmpty()) ? keyword : null;

            Page<UserDTO> pageUsers;

            if ("banned".equals(filter)) {
                pageUsers = userService.getAllUsersDTO(search, pageable);
                List<UserDTO> filteredUsers = pageUsers.getContent().stream()
                        .filter(dto -> {
                            if (dto == null) return false;
                            User user = userService.getUserById(dto.getId());
                            return user != null && !user.isAccountNonLocked();
                        })
                        .collect(Collectors.toList());

                model.addAttribute("users", filteredUsers);

            } else if ("strikes".equals(filter)) {
                pageUsers = userService.getAllUsersDTO(search, pageable);

                List<UserDTO> filteredUsers = pageUsers.getContent().stream()
                        .filter(dto -> {
                            if (dto == null) return false;
                            User user = userService.getUserById(dto.getId());
                            return user != null && user.getStrikes() > 0;
                        })
                        .collect(Collectors.toList());

                model.addAttribute("users", filteredUsers);

            } else {
                pageUsers = userService.getAllUsersDTO(search, pageable);

                List<UserDTO> filteredUsers = pageUsers.getContent().stream()
                        .filter(dto -> dto != null)
                        .collect(Collectors.toList());

                model.addAttribute("users", filteredUsers);
            }

            if (search != null) {
                model.addAttribute("keyword", search);
            }
            if (filter != null) {
                model.addAttribute("filter", filter);
            }

            model.addAttribute("currentPage", page);
            model.addAttribute("totalItems", pageUsers.getTotalElements());
            model.addAttribute("totalPages", pageUsers.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("entitytype", "user");

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "views/users/user-all";
    }

    @PostMapping("/admin/users/{userId}/unban")
    @PreAuthorize("hasRole('ADMIN')")
    public String unbanUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            userService.unbanUser(userId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "User " + user.getUsername() + " wurde entsperrt und Strikes zurückgesetzt");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Fehler beim Entsperren: " + e.getMessage());
        }

        return "redirect:/admin/all";
    }

    @PostMapping("/admin/users/{userId}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public String banUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            userService.banUser(userId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "User " + user.getUsername() + " wurde manuell gesperrt");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Fehler beim Sperren: " + e.getMessage());
        }

        return "redirect:/admin/all";
    }

    @PostMapping("/admin/users/{userId}/reset-strikes")
    @PreAuthorize("hasRole('ADMIN')")
    public String resetStrikes(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            int oldStrikes = user.getStrikes();
            userService.resetStrikes(userId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Strikes für " + user.getUsername() + " wurden zurückgesetzt (von " + oldStrikes + " auf 0)");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Fehler beim Zurücksetzen: " + e.getMessage());
        }

        return "redirect:/admin/all";
    }
}