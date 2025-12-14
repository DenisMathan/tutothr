package tutothr.course;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private CourseRepositoryI courseRepository;
    
    public CourseService (CourseRepositoryI courseRepository) {
        this.courseRepository = courseRepository;
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
