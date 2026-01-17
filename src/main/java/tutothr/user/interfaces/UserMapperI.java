package tutothr.user.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import tutothr.user.User;
import tutothr.auth.dtos.RegisterUserDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapperI {
    RegisterUserDTO toDTO(User user);
    User toEntity(RegisterUserDTO dto);

    // List<ChapterDTO> toChapterDTOs(List<Chapter> chapters);

    // List<Chapter> toEntities(List<ChapterDTO> chapterDTOs);
}
