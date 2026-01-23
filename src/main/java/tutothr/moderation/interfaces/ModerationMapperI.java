package tutothr.moderation.interfaces;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import tutothr.moderation.Report;
import tutothr.moderation.ReportDTO;
import tutothr.user.interfaces.UserRepositoryI;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ModerationMapperI {

    @Autowired
    protected UserRepositoryI userRepository;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "reporter.username", target = "reporterUsername")
    @Mapping(source = "reason", target = "reason")
    @Mapping(source = "reportedAt", target = "reportedAt")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "targetId", ignore = true)
    @Mapping(target = "contentPreview", ignore = true)
    @Mapping(target = "contextInfo", ignore = true)
    @Mapping(target = "offenderId", ignore = true)
    @Mapping(target = "offenderUsername", ignore = true)
    @Mapping(target = "offenderStrikes", ignore = true)
    public abstract ReportDTO toDto(Report report);

    public abstract List<ReportDTO> toDtos(List<Report> reports);

    @AfterMapping
    protected void fillGenericFields(Report report, @MappingTarget ReportDTO dto) {
        if (report.getType() == null) return;

        dto.setType(report.getType().name());

        switch (report.getType()) {
            case MESSAGE:
                if (report.getMessage() != null) {
                    dto.setTargetId(report.getMessage().getId());
                    dto.setContentPreview(report.getMessage().getContent());
                    dto.setContextInfo("Chat Nachricht");

                    var sender = report.getMessage().getSender();
                    if (sender != null) {
                        dto.setOffenderId(sender.getId());
                        dto.setOffenderUsername(sender.getUsername());
                        dto.setOffenderStrikes(sender.getStrikes());
                    }
                }
                break;

            case COURSE:
                if (report.getCourse() != null) {
                    dto.setTargetId(report.getCourse().getId());
                    dto.setContentPreview(report.getCourse().getTitle());
                    dto.setContextInfo("Kurs");

                    Long creatorId = report.getCourse().getOwnerId();
                    if (creatorId != null) {
                        userRepository.findById(creatorId).ifPresent(creator -> {
                            dto.setOffenderId(creator.getId());
                            dto.setOffenderUsername(creator.getUsername());
                            dto.setOffenderStrikes(creator.getStrikes());
                        });
                    }
                }
                break;

            case CHAPTER:
                if (report.getChapter() != null) {
                    dto.setTargetId(report.getChapter().getId());
                    dto.setContentPreview(report.getChapter().getTitle());

                    String courseTitle = "?";
                    Long courseCreatorId = null;

                    if (report.getChapter().getCourse() != null) {
                        courseTitle = report.getChapter().getCourse().getTitle();
                        courseCreatorId = report.getChapter().getCourse().getOwnerId();
                    }

                    dto.setContextInfo("Kapitel in: " + courseTitle);

                    if (courseCreatorId != null) {
                        userRepository.findById(courseCreatorId).ifPresent(creator -> {
                            dto.setOffenderId(creator.getId());
                            dto.setOffenderUsername(creator.getUsername());
                            dto.setOffenderStrikes(creator.getStrikes());
                        });
                    }
                }
                break;
            case HASHTAG:
                if(report.getHashtag() != null) {
                    dto.setTargetId(report.getHashtag().getId());
                    dto.setContentPreview(report.getHashtag().getName());
                    dto.setContextInfo("Hashtag");
                    // Hashtags haben keinen globalen Creator mehr - kein Offender
                }
                break;
        }
    }
}