package tutothr.moderation;

import jakarta.persistence.*;
import tutothr.hashtag.Hashtag;
import tutothr.message.Message;
import tutothr.course.Course;
import tutothr.chapter.Chapter;
import tutothr.user.User;
import java.time.LocalDateTime;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User reporter;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    private String reason;
    private LocalDateTime reportedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    public Report() {}

    public Report(User reporter, Message message, String reason) {
        this.reporter = reporter;
        this.message = message;
        this.type = ReportType.MESSAGE;
        this.reason = reason;
    }

    public Report(User reporter, Course course, String reason) {
        this.reporter = reporter;
        this.course = course;
        this.type = ReportType.COURSE;
        this.reason = reason;
    }

    public Report(User reporter, Chapter chapter, String reason) {
        this.reporter = reporter;
        this.chapter = chapter;
        this.type = ReportType.CHAPTER;
        this.reason = reason;
    }

    public Report(User reporter, Hashtag hashtag, String reason) {
        this.reporter = reporter;
        this.hashtag = hashtag;
        this.type = ReportType.HASHTAG;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }
    public ReportType getType() { return type; }
    public void setType(ReportType type) { this.type = type; }
    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Chapter getChapter() { return chapter; }
    public void setChapter(Chapter chapter) { this.chapter = chapter; }
    public Hashtag getHashtag() {
        return hashtag;
    }
    public void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
}