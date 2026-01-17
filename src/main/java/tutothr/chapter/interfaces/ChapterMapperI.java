package tutothr.chapter.interfaces;

import java.util.List;

import org.mapstruct.Mapper;

import tutothr.chapter.Chapter;
import tutothr.chapter.ChapterDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ChapterMapperI {
    ChapterDTO toDTO(Chapter chapter);

    Chapter toEntity(ChapterDTO dto);

    List<ChapterDTO> toChapterDTOs(List<Chapter> chapters);

    List<Chapter> toEntities(List<ChapterDTO> chapterDTOs);
}
