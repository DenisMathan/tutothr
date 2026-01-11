package tutothr.moderation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import tutothr.auth.config.MyUserDetails;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    @PostMapping("/messages/{id}/report")
    @ResponseBody
    public ResponseEntity<Map<String, String>> reportMessage(@PathVariable Long id,
                                                             @RequestParam String reason) {
        Map<String, String> response = new HashMap<>();

        try {
            Long currentUserId = getCurrentUserId();
            moderationService.reportMessage(currentUserId, id, reason);
            response.put("status", "success");
            response.put("message", "Nachricht wurde gemeldet");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Ein Fehler ist aufgetreten");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/admin/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public String showReports(Model model) {
        model.addAttribute("reports", moderationService.getPendingReports());
        model.addAttribute("pendingCount", moderationService.getPendingReportsCount());
        return "views/admin/reports";
    }

    @PostMapping("/admin/reports/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public String resolveReport(@PathVariable Long id,
                                @RequestParam boolean strike,
                                Model model) {
        try {
            moderationService.resolveReport(id, strike);
            model.addAttribute("successMessage", "Report erfolgreich bearbeitet");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Fehler beim Bearbeiten: " + e.getMessage());
        }
        return "redirect:/admin/reports";
    }

    private Long getCurrentUserId() {
        return ((MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getId();
    }
}