package tutothr.user.interfaces;

import org.mapstruct.Mapper;
import tutothr.user.User;
import tutothr.auth.dtos.RegisterUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapperI {
    RegisterUserDTO toDTO(User user);
    User toEntity(RegisterUserDTO dto);

    // List<ChapterDTO> toChapterDTOs(List<Chapter> chapters);

    // List<Chapter> toEntities(List<ChapterDTO> chapterDTOs);
}
