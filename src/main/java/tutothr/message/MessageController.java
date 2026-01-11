package tutothr.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutothr.auth.config.MyUserDetails;
import tutothr.moderation.ModerationService;
import tutothr.user.User;
import tutothr.user.UserService;
import tutothr.course.Course;
import tutothr.course.CourseService;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ModerationService moderationService;

    @GetMapping("/inbox")
    public String showInbox(Model model) {
        Long currentUserId = getCurrentUserId();

        List<ConversationDTO> conversations = messageService.getConversations(currentUserId);

        // Anzahl ungelesener Nachrichten
        long unreadCount = messageService.getUnreadCount(currentUserId);

        model.addAttribute("conversations", conversations);
        model.addAttribute("unreadCount", unreadCount);

        // Anzahl offener Reports für Admin-Badge
        try {
            long pendingReports = moderationService.getPendingReportsCount();
            model.addAttribute("pendingReportsCount", pendingReports);
        } catch (Exception e) {
            model.addAttribute("pendingReportsCount", 0);
        }

        return "messages/inbox";
    }

    @GetMapping("/new")
    public String showNewMessageForm(
            @RequestParam Long recipientId,
            @RequestParam(required = false) Long courseId,
            Model model) {

        User recipient = userService.getUserById(recipientId);
        model.addAttribute("recipient", recipient);

        if (courseId != null) {
            Course course = courseService.findById(courseId);
            model.addAttribute("course", course);
        }

        return "messages/new-message";
    }

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam Long recipientId,
            @RequestParam String content,
            @RequestParam(required = false) Long courseId) {

        Long senderId = getCurrentUserId();

        Message message = messageService.sendMessage(senderId, recipientId, content, courseId);

        User recipient = userService.getUserById(recipientId);

        // Hier nutzen wir bereits das MessageDTO für den WebSocket
        MessageDTO dto = new MessageDTO(message);

        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/messages",
                dto
        );

        return "redirect:/messages/inbox";
    }

    @GetMapping("/conversation/{userId}")
    public String showConversation(@PathVariable Long userId, Model model) {
        Long currentUserId = getCurrentUserId();

        List<Message> messages = messageService.getConversation(currentUserId, userId);
        User otherUser = userService.getUserById(userId);

        messageService.markConversationAsRead(currentUserId, userId);

        model.addAttribute("messages", messages);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("currentUserId", currentUserId);

        return "messages/conversation";
    }

    @PostMapping("/conversation/{userId}/reply")
    public String replyToConversation(
            @PathVariable Long userId,
            @RequestParam String content) {

        Long senderId = getCurrentUserId();
        messageService.sendMessage(senderId, userId, content, null);

        return "redirect:/messages/conversation/" + userId;
    }

    private Long getCurrentUserId() {
        return ((MyUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getId();
    }
}