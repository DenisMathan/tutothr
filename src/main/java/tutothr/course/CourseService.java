package tutothr.course;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.common.BaseService;
import tutothr.common.models.Field;

@Service
public class CourseService extends BaseService {

    private CourseRepositoryI courseRepository;
    private CoursePermissionService coursePermissionService;

    public CourseService(CourseRepositoryI courseRepository, CoursePermissionService coursePermissionService) {
        super();
        this.courseRepository = courseRepository;
        this.coursePermissionService = coursePermissionService;
    }

    @Override
    public void init() {
        fields = List.of(
                new Field("title", "Titel", "text"),
                new Field("description", "Beschreibung", "textarea"),
                new Field("price", "Preis", "number"));
    }

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        courses = coursePermissionService.setOwner(courses);
        return courses;
    }

    public Course getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course != null) {
            course.setIsOwner(coursePermissionService.isCurrentUserOwner(course.getOwnerId()));
        }
        return courseRepository.findById(id).orElse(null);
    }

    public void saveCourse(Course course) {
        courseRepository.save(course);
    }

    public void deleteCourseById(Long id) {
        courseRepository.findById(id).ifPresent(course -> {
            courseRepository.delete(course);
        });
    }
}
