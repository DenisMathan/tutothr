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

        if (senderId == null) return;

        Message message = messageService.sendMessage(
                senderId,
                request.getReceiverId(),
                request.getContent(),
                request.getCourseId()
        );

        ChatMessageDTO dto = new ChatMessageDTO(message);

        User receiver = userService.getUserById(request.getReceiverId());

        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/messages",
                dto
        );

        User sender = userService.getUserById(senderId);

        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/messages",
                dto
        );

        messagingTemplate.convertAndSendToUser(
                sender.getUsername(),
                "/queue/messages",
                dto
        );
    }

    @MessageMapping("/typing")
    public void userTyping(@Payload TypingNotification notification) {
        Long senderId = getCurrentUserId();

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(senderId);
        dto.setReceiverId(notification.getReceiverId());
        dto.setType(ChatMessageDTO.MessageType.TYPING);

        messagingTemplate.convertAndSendToUser(
                notification.getReceiverId().toString(),
                "/queue/typing",
                dto
        );
    }

    @MessageMapping("/read")
    public void markAsRead(@Payload ReadNotification notification) {
        Long currentUserId = getCurrentUserId();
        messageService.markConversationAsRead(currentUserId, notification.getOtherUserId());

        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setSenderId(currentUserId);
        dto.setReceiverId(notification.getOtherUserId());
        dto.setType(ChatMessageDTO.MessageType.READ);

        messagingTemplate.convertAndSendToUser(
                notification.getOtherUserId().toString(),
                "/queue/read",
                dto
        );
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
        private Long receiverId;

        public Long getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(Long receiverId) {
            this.receiverId = receiverId;
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