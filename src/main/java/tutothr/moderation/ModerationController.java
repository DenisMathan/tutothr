package tutothr.moderation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import tutothr.auth.config.AppPrincipal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ModerationController {

    @Autowired private ModerationService moderationService;

    @PostMapping("/messages/{id}/report")
    @ResponseBody
    public ResponseEntity<?> reportMessage(@PathVariable Long id, @RequestParam String reason, Principal principal) {
        return handleReport(() -> moderationService.reportMessage(getCurrentUserId(principal), id, reason));
    }

    @PostMapping("/courses/{id}/report")
    @ResponseBody
    public ResponseEntity<?> reportCourse(@PathVariable Long id, @RequestParam String reason, Principal principal) {
        return handleReport(() -> moderationService.reportCourse(getCurrentUserId(principal), id, reason));
    }

    @PostMapping("/chapters/{id}/report")
    @ResponseBody
    public ResponseEntity<?> reportChapter(@PathVariable Long id, @RequestParam String reason, Principal principal) {
        return handleReport(() -> moderationService.reportChapter(getCurrentUserId(principal), id, reason));
    }

    @PostMapping("/hashtags/{id}/report")
    @ResponseBody
    public ResponseEntity<?> reportHashtag(@PathVariable Long id, @RequestParam String reason, Principal principal) {
        return handleReport(() -> moderationService.reportHashtag(getCurrentUserId(principal), id, reason));
    }

    // Helper für JSON Responses
    private ResponseEntity<?> handleReport(Runnable action) {
        Map<String, String> response = new HashMap<>();
        try {
            action.run();
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Moderations View
    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public String showReports(Model model) {
        model.addAttribute("reports", moderationService.getPendingReports());
        model.addAttribute("pendingCount", moderationService.getPendingReportsCount());
        return "views/admin/reports";
    }

    // Report resolven
    @PostMapping("/admin/reports/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public String resolveReport(@PathVariable Long id, @RequestParam boolean strike) {
        moderationService.resolveReport(id, strike);
        return "redirect:/admin/reports";
    }

    private Long getCurrentUserId(Principal principal) {
        if (principal instanceof Authentication auth) {
            Object p = auth.getPrincipal();
            if (p instanceof AppPrincipal appPrincipal) {
                return appPrincipal.getId();
            }
        }
        return null;
    }
}