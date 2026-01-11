package tutothr.message;


public class SendMessageRequest {

    private Long recipientId;
    private String content;
    private Long courseId;

    public SendMessageRequest() {}

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "SendMessageRequest{" +
                "recipientId=" + recipientId +
                ", content='" + content + '\'' +
                ", courseId=" + courseId +
                '}';
    }
}