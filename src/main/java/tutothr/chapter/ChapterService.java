package tutothr.chapter;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.chapter.interfaces.ChapterMapperI;
import tutothr.chapter.interfaces.ChapterRepositoryI;
import tutothr.common.BaseService;
import tutothr.common.models.Field;

@Service
public class ChapterService extends BaseService<ChapterDTO, Chapter> {

    private ChapterPermissionService chapterPermissionService;
    private ChapterMapperI mapper;

    public ChapterService(ChapterRepositoryI chapterRepository, ChapterPermissionService chapterPermissionService, ChapterMapperI mapper) {
        super(chapterRepository);
        this.chapterPermissionService = chapterPermissionService;
        this.mapper = mapper;
    }

    @Override
    public ChapterDTO mapToDTO(Chapter entity) {
        return mapper.toDTO(entity);
    }

    @Override
    public Chapter mapToEntity(ChapterDTO dto) {
        return mapper.toEntity(dto);
    }
}
