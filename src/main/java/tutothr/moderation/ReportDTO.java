package tutothr.moderation;

import java.time.LocalDateTime;

public class ReportDTO {
    private Long id;
    private Long reporterId;
    private String reporterUsername;
    private Long messageId;
    private String messageContent;
    private Long offenderId;
    private String offenderUsername;
    private int offenderStrikes;
    private String reason;
    private LocalDateTime reportedAt;
    private ReportStatus status;

    public ReportDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterUsername() {
        return reporterUsername;
    }

    public void setReporterUsername(String reporterUsername) {
        this.reporterUsername = reporterUsername;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getOffenderId() {
        return offenderId;
    }

    public void setOffenderId(Long offenderId) {
        this.offenderId = offenderId;
    }

    public String getOffenderUsername() {
        return offenderUsername;
    }

    public void setOffenderUsername(String offenderUsername) {
        this.offenderUsername = offenderUsername;
    }

    public int getOffenderStrikes() {
        return offenderStrikes;
    }

    public void setOffenderStrikes(int offenderStrikes) {
        this.offenderStrikes = offenderStrikes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }
}