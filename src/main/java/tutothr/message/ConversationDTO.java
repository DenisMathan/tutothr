package tutothr.message;

import java.time.LocalDateTime;


public class ConversationDTO {

    private Long otherUserId;
    private String otherUserUsername;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private long unreadCount;
    private boolean lastMessageFromMe;

    public ConversationDTO() {}

    public ConversationDTO(Long otherUserId, String otherUserUsername, String lastMessage,
                           LocalDateTime lastMessageTime, long unreadCount, boolean lastMessageFromMe) {
        this.otherUserId = otherUserId;
        this.otherUserUsername = otherUserUsername;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
        this.lastMessageFromMe = lastMessageFromMe;
    }

    // Getters and Setters
    public Long getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUserUsername() {
        return otherUserUsername;
    }

    public void setOtherUserUsername(String otherUserUsername) {
        this.otherUserUsername = otherUserUsername;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isLastMessageFromMe() {
        return lastMessageFromMe;
    }

    public void setLastMessageFromMe(boolean lastMessageFromMe) {
        this.lastMessageFromMe = lastMessageFromMe;
    }

    @Override
    public String toString() {
        return "ConversationDTO{" +
                "otherUserId=" + otherUserId +
                ", otherUserUsername='" + otherUserUsername + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTime=" + lastMessageTime +
                ", unreadCount=" + unreadCount +
                ", lastMessageFromMe=" + lastMessageFromMe +
                '}';
    }
}
