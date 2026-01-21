package tutothr.message;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import tutothr.auth.config.AppPrincipal;
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
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        Long senderId = getUserIdFromPrincipal(principal);

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
            
            // Send to recipient
            // Strategy: Send to both Username and Email to cover all login types (MyUserDetails vs OidcUser)
            // If the user is not connected on one of these channels, the broker will simply ignore it.
            if (recipient.getUsername() != null) {
                messagingTemplate.convertAndSendToUser(
                        recipient.getUsername(),
                        "/queue/messages",
                        dto
                );
            }
            if (recipient.getEmail() != null) {
                messagingTemplate.convertAndSendToUser(
                        recipient.getEmail(),
                        "/queue/messages",
                        dto
                );
            }

            // Echo to sender using their current session principal name
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/queue/messages",
                    dto
            );

        } catch (Exception e) {
            System.err.println("WebSocket sendMessage Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @MessageMapping("/typing")
    public void userTyping(@Payload TypingNotification notification, Principal principal) {
        Long senderId = getUserIdFromPrincipal(principal);

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
    public void markAsRead(@Payload ReadNotification notification, Principal principal) {
        Long currentUserId = getUserIdFromPrincipal(principal);

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

    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal instanceof Authentication auth) {
            Object p = auth.getPrincipal();
            if (p instanceof AppPrincipal appPrincipal) {
                return appPrincipal.getId();
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