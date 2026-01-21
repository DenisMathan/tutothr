package tutothr.message;

import java.time.LocalDateTime;

public class MessageDTO {

    private Long id;
    private String content;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private LocalDateTime sentAt;
    private MessageType type;
    private boolean read;

    public enum MessageType {
        CHAT,
        TYPING,
        READ
    }

    public MessageDTO() {
        this.type = MessageType.CHAT;
    }

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.senderId = message.getSender().getId();
        this.senderName = message.getSender().getUsername();
        this.receiverId = message.getReceiver().getId();
        this.sentAt = message.getSentAt();
        this.read = message.isRead();
        this.type = MessageType.CHAT;
    }

    public static MessageDTO createTypingNotification(Long senderId, Long receiverId) {
        MessageDTO dto = new MessageDTO();
        dto.setSenderId(senderId);
        dto.setReceiverId(receiverId);
        dto.setType(MessageType.TYPING);
        return dto;
    }

    public static MessageDTO createReadNotification(Long senderId, Long receiverId) {
        MessageDTO dto = new MessageDTO();
        dto.setSenderId(senderId);
        dto.setReceiverId(receiverId);
        dto.setType(MessageType.READ);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", senderName='" + senderName + '\'' +
                ", receiverId=" + receiverId +
                ", type=" + type +
                ", read=" + read +
                ", sentAt=" + sentAt +
                '}';
    }
}