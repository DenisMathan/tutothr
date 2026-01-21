package tutothr.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutothr.message.interfaces.MessageRepositoryI;
import tutothr.user.User;
import tutothr.user.UserService;
import tutothr.course.Course;
import tutothr.course.CourseService;

import java.util.*;

@Service
public class MessageService {

    @Autowired
    private MessageRepositoryI messageRepositoryI;

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

        return messageRepositoryI.save(message);
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        return messageRepositoryI.findConversation(userId1, userId2);
    }

    public long getUnreadCount(Long userId) {
        return messageRepositoryI.countByReceiverIdAndReadFalse(userId);
    }

    @Transactional
    public void markConversationAsRead(Long currentUserId, Long otherUserId) {
        List<Message> messages = messageRepositoryI.findConversation(currentUserId, otherUserId);
        messages.stream()
                .filter(m -> m.getReceiver().getId().equals(currentUserId))
                .filter(m -> !m.isRead())
                .forEach(m -> {
                    m.setRead(true);
                    messageRepositoryI.save(m);
                });
    }

    public List<ConversationDTO> getConversations(Long userId) {
        List<Message> allMessages = getAllMessagesByUserId(userId);

        Map<Long, List<Message>> conversationMap = new HashMap<>();

        // Nachrichten nach Gesprächspartner gruppieren
        for (Message msg : allMessages) {
            Long otherUserId = msg.getSender().getId().equals(userId)
                    ? msg.getReceiver().getId()
                    : msg.getSender().getId();

            conversationMap
                    .computeIfAbsent(otherUserId, k -> new ArrayList<>())
                    .add(msg);
        }

        List<ConversationDTO> previews = new ArrayList<>();

        for (Map.Entry<Long, List<Message>> entry : conversationMap.entrySet()) {
            List<Message> messages = entry.getValue();

            // Sortierung: Erst nach Zeit (neueste zuerst), dann nach ID
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

            // Ermitteln, wer der "andere" User in dieser Konversation ist
            User otherUser = latestMessage.getSender().getId().equals(userId)
                    ? latestMessage.getReceiver()
                    : latestMessage.getSender();

            // DTO erstellen
            ConversationDTO dto = new ConversationDTO(
                    otherUser.getId(),
                    otherUser.getUsername(),
                    latestMessage.getContent(),
                    latestMessage.getSentAt(),
                    unreadCount,
                    latestMessage.getSender().getId().equals(userId)
            );

            previews.add(dto);
        }

        // Konversationen nach der allerneuesten Nachricht sortieren
        previews.sort((p1, p2) -> p2.getLastMessageTime().compareTo(p1.getLastMessageTime()));

        return previews;
    }

    private List<Message> getAllMessagesByUserId(Long userId) {
        List<Message> sent = messageRepositoryI.findBySenderIdOrderBySentAtDesc(userId);
        List<Message> received = messageRepositoryI.findByReceiverIdOrderBySentAtDesc(userId);

        List<Message> all = new ArrayList<>();
        all.addAll(sent);
        all.addAll(received);

        return all;
    }
}