package tutothr.moderation;

import java.time.LocalDateTime;

public class ReportDTO {
    private Long id;
    private Long reporterId;
    private String reporterUsername;

    private String type;
    private Long targetId;
    private String contentPreview;
    private String contextInfo;

    private Long offenderId;
    private String offenderUsername;
    private int offenderStrikes;

    private String reason;
    private LocalDateTime reportedAt;
    private ReportStatus status;

    public ReportDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }
    public String getReporterUsername() { return reporterUsername; }
    public void setReporterUsername(String reporterUsername) { this.reporterUsername = reporterUsername; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public String getContentPreview() { return contentPreview; }
    public void setContentPreview(String contentPreview) { this.contentPreview = contentPreview; }
    public String getContextInfo() { return contextInfo; }
    public void setContextInfo(String contextInfo) { this.contextInfo = contextInfo; }
    public Long getOffenderId() { return offenderId; }
    public void setOffenderId(Long offenderId) { this.offenderId = offenderId; }
    public String getOffenderUsername() { return offenderUsername; }
    public void setOffenderUsername(String offenderUsername) { this.offenderUsername = offenderUsername; }
    public int getOffenderStrikes() { return offenderStrikes; }
    public void setOffenderStrikes(int offenderStrikes) { this.offenderStrikes = offenderStrikes; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
}