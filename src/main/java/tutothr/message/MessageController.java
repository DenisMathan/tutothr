package tutothr.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tutothr.auth.config.AppPrincipal;
import tutothr.common.services.MailService;
import tutothr.message.interfaces.MessageRepositoryI;
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

    @Autowired
    private MailService mailService;

    @Autowired
    private MessageRepositoryI messageRepository;

    @GetMapping("/inbox")
    public String showInbox(@RequestParam(defaultValue = "0") int page, Model model) {
        Long currentUserId = getCurrentUserId();

        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Message> conversationPage = messageRepository.findLatestConversations(currentUserId, pageable);

        model.addAttribute("conversations", conversationPage.getContent());
        model.addAttribute("currentPage", conversationPage.getNumber());
        model.addAttribute("totalPages", conversationPage.getTotalPages());
        model.addAttribute("totalItems", conversationPage.getTotalElements());
        model.addAttribute("currentUser", userService.getUserById(currentUserId));

        long unreadCount = messageService.getUnreadCount(currentUserId);
        model.addAttribute("unreadCount", unreadCount);

        try {
            long pendingReports = moderationService.getPendingReportsCount();
            model.addAttribute("pendingReportsCount", pendingReports);
        } catch (Exception e) {
            model.addAttribute("pendingReportsCount", 0);
        }

        return "views/messages/inbox";
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

        return "views/messages/new-message";
    }

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam Long recipientId,
            @RequestParam String content,
            @RequestParam(required = false) Long courseId) {

        Long senderId = getCurrentUserId();

        Message message = messageService.sendMessage(senderId, recipientId, content, courseId);

        User recipient = userService.getUserById(recipientId);
        Long currentUserId = getCurrentUserId();
        User sender = userService.getUserById(currentUserId);

        MessageDTO dto = new MessageDTO(message);

        messagingTemplate.convertAndSendToUser(
                recipient.getUsername(),
                "/queue/messages",
                dto
        );
        
        // Mail-Versand - Fehler abfangen, damit Nachricht trotzdem gesendet wird
        try {
            mailService.sendNewChatMail(recipient, sender);
        } catch (Exception e) {
            // Mail konnte nicht gesendet werden - Log und weitermachen
            System.err.println("Mail konnte nicht gesendet werden: " + e.getMessage());
        }
        
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

        return "/views/messages/conversation";
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
        return ((AppPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getId();
    }
}