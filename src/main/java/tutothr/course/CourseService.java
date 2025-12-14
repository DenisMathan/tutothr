package tutothr.course;

import java.util.List;

import org.springframework.stereotype.Service;

import tutothr.common.BaseService;
import tutothr.common.models.Field;

@Service
public class CourseService extends BaseService {

    private CourseRepositoryI courseRepository;
    
    public CourseService (CourseRepositoryI courseRepository) {
        super();
        this.courseRepository = courseRepository;
    }
    @Override
    public void init() {
        // TODO Auto-generated method stub
        fields = List.of(
            new Field("title", "Titel", "text"),
            new Field("description", "Beschreibung", "textarea"),
            new Field("price", "Preis", "number")
        );
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
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
