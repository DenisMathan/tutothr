package tutothr.moderation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutothr.chapter.Chapter;
import tutothr.chapter.ChapterService;
import tutothr.message.Message;
import tutothr.message.interfaces.MessageRepositoryI;
import tutothr.course.Course;
import tutothr.course.CourseService;
import tutothr.moderation.interfaces.ModerationMapperI;
import tutothr.moderation.interfaces.ReportRepositoryI;
import tutothr.user.User;
import tutothr.user.UserService;
import tutothr.user.interfaces.UserRepositoryI;

import java.util.List;

@Service
public class ModerationService {

    @Autowired
    private ReportRepositoryI reportRepository;

    @Autowired
    private UserRepositoryI userRepository;

    @Autowired
    private MessageRepositoryI messageRepositoryI;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModerationMapperI moderationMapperI;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private UserService userService;

    public void reportMessage(Long reporterId, Long messageId, String reason) {
        User reporter = userRepository.findById(reporterId).orElseThrow();
        Message message = messageRepositoryI.findById(messageId).orElseThrow();

        if(message.getSender().getId().equals(reporterId)) {
            throw new IllegalArgumentException("Eigene Nachricht kann nicht gemeldet werden");
        }
        if(reportRepository.existsByReporterAndMessageAndStatus(reporter, message, ReportStatus.PENDING)) {
            throw new IllegalArgumentException("Bereits gemeldet");
        }

        reportRepository.save(new Report(reporter, message, reason));
    }

    public void reportCourse(Long reporterId, Long courseId, String reason) {
        User reporter = userRepository.findById(reporterId).orElseThrow();
        Course course = courseService.findById(courseId);

        if(course.getOwnerId().equals(reporterId)) {
            throw new IllegalArgumentException("Eigener Kurs kann nicht gemeldet werden");
        }
        if(reportRepository.existsByReporterAndCourseAndStatus(reporter, course, ReportStatus.PENDING)) {
            throw new IllegalArgumentException("Bereits gemeldet");
        }

        reportRepository.save(new Report(reporter, course, reason));
    }

    public void reportChapter(Long reporterId, Long chaperId, String reason) {
        User reporter = userRepository.findById(reporterId).orElseThrow();
        Chapter chapter = chapterService.findById(chaperId);
        Course course = chapter.getCourse();

        if(course.getOwnerId().equals(reporterId)) {
            throw new IllegalArgumentException("Eigener Kurs kann nicht gemeldet werden");
        }
        if(reportRepository.existsByReporterAndChapterAndStatus(reporter, chapter, ReportStatus.PENDING)) {
            throw new IllegalArgumentException("Bereits gemeldet");
        }

        reportRepository.save(new Report(reporter, chapter, reason));
    }


    public List<ReportDTO> getPendingReports() {
        List<Report> reports = reportRepository.findByStatusOrderByReportedAtDesc(ReportStatus.PENDING);
        return moderationMapperI.toDtos(reports);
    }

    public long getPendingReportsCount() {
        return reportRepository.findByStatusOrderByReportedAtDesc(ReportStatus.PENDING).size();
    }

    @Transactional
    public void resolveReport(Long reportId, boolean issueStrike) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if(report.getStatus() != ReportStatus.PENDING) {
            throw new IllegalStateException("Report already resolved");
        }

        if (issueStrike) {
            report.setStatus(ReportStatus.ACCEPTED);

            // Bestimme den betroffenen User basierend auf Report-Typ
            User offender = getOffenderFromReport(report);

            if (offender != null) {
                boolean userWasBanned = userService.incrementStrikes(offender.getId());

                // weitere Aktionen bei Ban
                if (userWasBanned) {
                    handleUserBanned(offender, report);
                }
            } else {
                System.err.println("WARNUNG: Konnte Offender für Report " + reportId + " nicht finden!");
            }
        } else {
            // Report ignorieren
            report.setStatus(ReportStatus.REJECTED);
            System.out.println("ℹReport " + reportId + " wurde ignoriert");
        }

        reportRepository.save(report);
    }

    private User getOffenderFromReport(Report report) {
        switch (report.getType()) {
            case MESSAGE:
                return report.getMessage().getSender();

            case COURSE:
                Long courseOwnerId = report.getCourse().getOwnerId();
                return userRepository.findById(courseOwnerId).orElse(null);

            case CHAPTER:
                Long chapterOwnerId = report.getChapter().getCourse().getOwnerId();
                return userRepository.findById(chapterOwnerId).orElse(null);

            default:
                return null;
        }
    }

    private void handleUserBanned(User user, Report report) {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  USER AUTOMATISCH GESPERRT");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("User: " + user.getUsername() + " (ID: " + user.getId() + ")");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Strikes: " + user.getStrikes());
        System.out.println("Grund (letzter Report): " + report.getReason());
        System.out.println("Report-Typ: " + report.getType());
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // - emailService.sendBanNotification(user);
    }

    public List<User> getBannedUsers() {
        return userService.getBannedUsers();
    }

    public List<User> getUsersWithStrikes() {
        return userService.getUsersWithStrikes();
    }
}