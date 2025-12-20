package tutothr.course;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.category.CategoryDTO;
import tutothr.category.interfaces.CategoryRepositoryI;
import tutothr.chapter.ChapterDTO;
import tutothr.common.BaseService;
import tutothr.common.models.Field;
import tutothr.course.interfaces.CourseMapperI;
import tutothr.course.interfaces.CourseRepositoryI;

@Service
public class CourseService extends BaseService<CourseDTO, Course> {

    private CoursePermissionService coursePermissionService;
    private CourseMapperI mapper;

    public CourseService(CourseRepositoryI courseRepository, CoursePermissionService coursePermissionService, CourseMapperI mapper) {
        super(courseRepository);
        this.coursePermissionService = coursePermissionService;
        this.mapper = mapper;
    }

    // public List<Course> getAllCourses() {
    //     List<Course> courses = repository.findAll();
    //     courses = coursePermissionService.setOwner(courses);
    //     return courses;
    // }

    // @Override
    // public Course findById(Long id) {
    //     Course course = repository.findById(id).orElse(null);
    //     // if (course != null) {
    //     //     course.setIsOwner(coursePermissionService.isCurrentUserOwner(course.getOwnerId()));
    //     // }
    //     return repository.findById(id).orElse(null);
    // }

    // public void deleteById(Long id) {
    //     repository.findById(id).ifPresent(course -> {
    //         repository.delete(course);
    //     });
    // }

    @Override
    public CourseDTO mapToDTO(Course entity) {
        CourseDTO result = mapper.toDTO(entity);
        result.setIsOwner(coursePermissionService.isCurrentUserOwner(entity.getOwnerId()));
        if (result.getIsOwner()) {
            result.setAddChapter(new ChapterDTO());
            result.getAddChapter().setCourseId(entity.getId());
            System.out.println("Set courseId in addChapter to " + entity.getId());
        }
        return result;
    }

    @Override
    public Course mapToEntity(CourseDTO dto) {
        return mapper.toEntity(dto);
    }
}
