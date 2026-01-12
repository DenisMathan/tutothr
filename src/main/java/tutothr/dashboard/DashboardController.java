package tutothr.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tutothr.auth.config.MyUserDetails;
import tutothr.user.User;
import tutothr.user.UserService;

@Controller
public class DashboardController {

    @Autowired private DashboardService dashboardService;
    @Autowired private UserService userService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        User user = getCurrentUserById();

        DashboardDTO stats = dashboardService.getStatsForUser(user);

        model.addAttribute("stats", stats);
        model.addAttribute("user", user);

        return "/views/dashboard";
    }

    private User getCurrentUserById() {
        Long userId = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return userService.getUserById(userId);
    }
}
