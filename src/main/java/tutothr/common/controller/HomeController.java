package tutothr.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tutothr.auth.config.AppPrincipal;
import tutothr.dashboard.DashboardDTO;
import tutothr.dashboard.DashboardService;
import tutothr.moderation.ModerationService;
import tutothr.user.User;
import tutothr.user.UserService;

@Controller
public class HomeController {

	@Autowired
	UserService userService;
	@Autowired
	DashboardService dashboardService;
	@Autowired
	ModerationService moderationService;
	@Autowired
	tutothr.auth.AuthService authService;
    @RequestMapping(method = RequestMethod.GET, value = {"/home", "/"})
	public String home(Model model) {
		User user = getCurrentUserById();
		DashboardDTO stats = dashboardService.getStatsForUser(user);

		model.addAttribute("stats", stats);
		model.addAttribute("user", user);

		try {
			long pendingReports = moderationService.getPendingReportsCount();
			model.addAttribute("pendingReportsCount", pendingReports);
		} catch (Exception e) {
			model.addAttribute("pendingReportsCount", 0);
		}

		return "views/dashboard";
	}

	private User getCurrentUserById() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof AppPrincipal) {
			Long userId = ((AppPrincipal) principal).getId();
			return userService.getUserById(userId);
		}
		
		throw new RuntimeException("Unknown principal type: " + principal.getClass());
	}
}
