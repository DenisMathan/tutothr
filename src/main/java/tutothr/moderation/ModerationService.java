package tutothr.moderation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutothr.message.Message;
import tutothr.message.interfaces.MessageRepositoryI;
import tutothr.moderation.interfaces.ModerationMapperI;
import tutothr.moderation.interfaces.ReportRepositoryI;
import tutothr.user.User;
import tutothr.user.interfaces.UserRepositoryI;

import java.util.List;

@Service
public class ModerationService {

    @Autowired private ReportRepositoryI reportRepository;
    @Autowired private UserRepositoryI userRepository;
    @Autowired private MessageRepositoryI messageRepositoryI;
    @Autowired private ModerationMapperI moderationMapperI;

    public void reportMessage(Long reporterId, Long messageId, String reason) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));
        Message message = messageRepositoryI.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if(message.getSender().getId().equals(reporterId)) {
            throw new IllegalArgumentException("Cannot report your own message");
        }

        if(reportRepository.existsByReporterAndMessageAndStatus(reporter, message, ReportStatus.PENDING)) {
            throw new IllegalArgumentException("You have already reported this message");
        }

        Report report = new Report(reporter, message, reason);
        reportRepository.save(report);
    }

    public List<ReportDTO> getPendingReports() {
        List<Report> reports = reportRepository.findByStatusOrderByReportedAtDesc(ReportStatus.PENDING);
        // Umwandlung Entity -> DTO
        return moderationMapperI.toDtos(reports);
    }

    @Transactional
    public void resolveReport(Long reportId, boolean issueStrike) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if(report.getStatus() != ReportStatus.PENDING) {
            throw new IllegalStateException("Report has already been resolved");
        }

        if (issueStrike) {
            report.setStatus(ReportStatus.ACCEPTED);
            User offender = report.getMessage().getSender();

            int newStrikes = offender.getStrikes() + 1;
            offender.setStrikes(newStrikes);

            if (newStrikes >= 3) {
                offender.setAccountNonLocked(false);
            }
            userRepository.save(offender);
        } else {
            report.setStatus(ReportStatus.REJECTED);
        }

        reportRepository.save(report);
    }

    public List<ReportDTO> getReportsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Report> reports = reportRepository.findByStatusOrderByReportedAtDesc(ReportStatus.ACCEPTED);
        return moderationMapperI.toDtos(reports);
    }

    public long getPendingReportsCount() {
        return reportRepository.findByStatusOrderByReportedAtDesc(ReportStatus.PENDING).size();
    }
}