package tutothr.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import tutothr.auth.config.MyUserDetails;
import tutothr.user.User;
import tutothr.user.UserService;


@Controller
public class WebSocketMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @MessageMapping("/chat")
    public void sendMessage(@Payload SendMessageRequest request) {
        Long senderId = getCurrentUserId();

        if (senderId == null) {
            System.err.println("WebSocket: senderId is null");
            return;
        }

        Long recipientId = request.getRecipientId();
        if (recipientId == null) {
            System.err.println("WebSocket: recipientId is null");
            return;
        }

        try {
            Message message = messageService.sendMessage(
                    senderId,
                    recipientId,
                    request.getContent(),
                    request.getCourseId()
            );

            MessageDTO dto = new MessageDTO(message);

            User recipient = userService.getUserById(recipientId);
            User sender = userService.getUserById(senderId);

            messagingTemplate.convertAndSendToUser(
                    recipient.getUsername(),
                    "/queue/messages",
                    dto
            );

            messagingTemplate.convertAndSendToUser(
                    sender.getUsername(),
                    "/queue/messages",
                    dto
            );

        } catch (Exception e) {
            System.err.println("WebSocket sendMessage Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @MessageMapping("/typing")
    public void userTyping(@Payload TypingNotification notification) {
        Long senderId = getCurrentUserId();

        if (senderId == null || notification.getRecipientId() == null) {
            return;
        }

        try {
            MessageDTO dto = MessageDTO.createTypingNotification(
                    senderId,
                    notification.getRecipientId()
            );

            User recipient = userService.getUserById(notification.getRecipientId());

            messagingTemplate.convertAndSendToUser(
                    recipient.getUsername(),
                    "/queue/typing",
                    dto
            );
        } catch (Exception e) {
            System.err.println("Typing notification error: " + e.getMessage());
        }
    }

    @MessageMapping("/read")
    public void markAsRead(@Payload ReadNotification notification) {
        Long currentUserId = getCurrentUserId();

        if (currentUserId == null || notification.getOtherUserId() == null) {
            return;
        }

        try {
            messageService.markConversationAsRead(currentUserId, notification.getOtherUserId());

            MessageDTO dto = MessageDTO.createReadNotification(
                    currentUserId,
                    notification.getOtherUserId()
            );

            User otherUser = userService.getUserById(notification.getOtherUserId());

            messagingTemplate.convertAndSendToUser(
                    otherUser.getUsername(),
                    "/queue/read",
                    dto
            );
        } catch (Exception e) {
            System.err.println("Read notification error: " + e.getMessage());
        }
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof MyUserDetails) {
            return ((MyUserDetails) principal).getId();
        }

        if (principal instanceof OidcUser) {
            String sub = ((OidcUser) principal).getSubject();
            try {
                return Long.parseLong(sub);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    static class TypingNotification {
        private Long recipientId;

        public Long getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(Long recipientId) {
            this.recipientId = recipientId;
        }
    }

    static class ReadNotification {
        private Long otherUserId;

        public Long getOtherUserId() {
            return otherUserId;
        }

        public void setOtherUserId(Long otherUserId) {
            this.otherUserId = otherUserId;
        }
    }
}