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

    // --- ADMIN METHODEN ---

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

            User offender = null;
            switch (report.getType()) {
                case MESSAGE:
                    offender = report.getMessage().getSender();
                    break;

                case COURSE:
                    Long creatorId = report.getCourse().getOwnerId();
                    offender = userRepository.findById(creatorId).orElse(null);
                    break;

                case CHAPTER:
                    Long chapterCreatorId = report.getChapter().getCourse().getOwnerId();
                    offender = userRepository.findById(chapterCreatorId).orElse(null);
                    break;
            }

            if (offender != null) {
                int newStrikes = offender.getStrikes() + 1;
                offender.setStrikes(newStrikes);

                if (newStrikes >= 3) {
                    offender.setAccountNonLocked(false);
                }
                userRepository.save(offender);
            }
        } else {
            report.setStatus(ReportStatus.REJECTED);
        }

        reportRepository.save(report);
    }
}