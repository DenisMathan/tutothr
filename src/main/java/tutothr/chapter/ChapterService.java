package tutothr.chapter;

import org.springframework.stereotype.Service;

import tutothr.chapter.interfaces.ChapterMapperI;
import tutothr.chapter.interfaces.ChapterRepositoryI;
import tutothr.common.BaseService;
import tutothr.course.Course;
import tutothr.course.CourseDTO;
import tutothr.course.CourseService;
import tutothr.course.interfaces.CourseRepositoryI;

@Service
public class ChapterService extends BaseService<ChapterDTO, Chapter> {

    private ChapterPermissionService chapterPermissionService;
    private ChapterMapperI mapper;
    private CourseService courseService;

    public ChapterService(ChapterRepositoryI chapterRepository, ChapterPermissionService chapterPermissionService, ChapterMapperI mapper, CourseService courseService) {
        super(chapterRepository);

        this.chapterPermissionService = chapterPermissionService;
        this.mapper = mapper;
        this.courseService = courseService;
    }

    @Override
    public ChapterDTO mapToDTO(Chapter entity) {
        return mapper.toDTO(entity);
    }

    @Override
    public Chapter mapToEntity(ChapterDTO dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public void saveDTO(ChapterDTO chapterDTO) {
        Chapter chapter = mapToEntity(chapterDTO);
        chapter.setCourse(this.courseService.findById(chapterDTO.getCourseId()));
        repository.save(chapter);
    }

    public CourseDTO findCourseDTOById(Long id) {
        return this.courseService.findDTOById(id);
    }
}
