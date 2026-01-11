package tutothr.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutothr.user.User;
import tutothr.user.UserService;
import tutothr.course.Course;
import tutothr.course.CourseService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;


    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String content, Long courseId) {
        User sender = userService.getUserById(senderId);
        User receiver = userService.getUserById(receiverId);

        Message message = new Message(content, sender, receiver);

        if (courseId != null) {
            Course course = courseService.findById(courseId);
            message.setCourse(course);
        }

        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        return messageRepository.findConversation(userId1, userId2);
    }

    public long getUnreadCount(Long userId) {
        return messageRepository.countByReceiverIdAndReadFalse(userId);
    }

    @Transactional
    public void markConversationAsRead(Long currentUserId, Long otherUserId) {
        List<Message> messages = messageRepository.findConversation(currentUserId, otherUserId);
        messages.stream()
                .filter(m -> m.getReceiver().getId().equals(currentUserId))
                .filter(m -> !m.isRead())
                .forEach(m -> {
                    m.setRead(true);
                    messageRepository.save(m);
                });
    }


    public List<ConversationPreview> getConversations(Long userId) {
        List<Message> allMessages = getAllMessagesByUserId(userId);

        Map<Long, List<Message>> conversationMap = new HashMap<>();

        for (Message msg : allMessages) {
            Long otherUserId = msg.getSender().getId().equals(userId)
                    ? msg.getReceiver().getId()
                    : msg.getSender().getId();

            conversationMap
                    .computeIfAbsent(otherUserId, k -> new ArrayList<>())
                    .add(msg);
        }

        List<ConversationPreview> previews = new ArrayList<>();

        for (Map.Entry<Long, List<Message>> entry : conversationMap.entrySet()) {
            List<Message> messages = entry.getValue();
            messages.sort((m1, m2) -> {
                int timeComparison = m2.getSentAt().compareTo(m1.getSentAt());
                if (timeComparison != 0) {
                    return timeComparison;
                }
                return m2.getId().compareTo(m1.getId());
            });

            Message latestMessage = messages.get(0);

            long unreadCount = messages.stream()
                    .filter(m -> m.getReceiver().getId().equals(userId))
                    .filter(m -> !m.isRead())
                    .count();

            User otherUser = latestMessage.getSender().getId().equals(userId)
                    ? latestMessage.getReceiver()
                    : latestMessage.getSender();

            ConversationPreview preview = new ConversationPreview(
                    otherUser,
                    latestMessage.getContent(),
                    latestMessage.getSentAt(),
                    unreadCount,
                    latestMessage.getSender().getId().equals(userId)
            );

            previews.add(preview);
        }

        previews.sort((p1, p2) -> p2.getLastMessageTime().compareTo(p1.getLastMessageTime()));

        return previews;
    }

    private List<Message> getAllMessagesByUserId(Long userId) {
        List<Message> sent = messageRepository.findBySenderIdOrderBySentAtDesc(userId);
        List<Message> received = messageRepository.findByReceiverIdOrderBySentAtDesc(userId);

        List<Message> all = new ArrayList<>();
        all.addAll(sent);
        all.addAll(received);

        return all;
    }


    public static class ConversationPreview {
        private User otherUser;
        private String lastMessage;
        private LocalDateTime lastMessageTime;
        private long unreadCount;
        private boolean lastMessageFromMe;

        public ConversationPreview(User otherUser, String lastMessage,
                                   LocalDateTime lastMessageTime, long unreadCount,
                                   boolean lastMessageFromMe) {
            this.otherUser = otherUser;
            this.lastMessage = lastMessage;
            this.lastMessageTime = lastMessageTime;
            this.unreadCount = unreadCount;
            this.lastMessageFromMe = lastMessageFromMe;
        }

        public User getOtherUser() { return otherUser; }
        public String getLastMessage() { return lastMessage; }
        public LocalDateTime getLastMessageTime() { return lastMessageTime; }
        public long getUnreadCount() { return unreadCount; }
        public boolean isLastMessageFromMe() { return lastMessageFromMe; }
    }
}