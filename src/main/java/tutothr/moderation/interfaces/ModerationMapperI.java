package tutothr.moderation.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tutothr.moderation.Report;
import tutothr.moderation.ReportDTO;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModerationMapperI {

    // Mapping: Entity -> DTO

    @Mapping(source = "id", target = "id")
    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "reporter.username", target = "reporterUsername")

    @Mapping(source = "message.id", target = "messageId")
    @Mapping(source = "message.content", target = "messageContent")

    @Mapping(source = "message.sender.id", target = "offenderId")
    @Mapping(source = "message.sender.username", target = "offenderUsername")
    @Mapping(source = "message.sender.strikes", target = "offenderStrikes")

    @Mapping(source = "reason", target = "reason")
    @Mapping(source = "reportedAt", target = "reportedAt")
    @Mapping(source = "status", target = "status")
    ReportDTO toDto(Report report);

    List<ReportDTO> toDtos(List<Report> reports);
}
