package tutothr.moderation.interfaces;

import tutothr.common.MyBaseRepository;
import tutothr.message.Message;
import tutothr.moderation.Report;
import tutothr.moderation.ReportStatus;
import tutothr.user.User;

import java.util.List;

public interface ReportRepositoryI extends MyBaseRepository<Report, Long> {
    List<Report> findByStatusOrderByReportedAtDesc(ReportStatus status);

    boolean existsByReporterAndMessageAndStatus(User reporter, Message message, ReportStatus status);
}