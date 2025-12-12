package tutothr.course;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CourseController {
    private CourseService courseService;
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String getMethodName(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "/views/courses/courses";
    }

    @GetMapping("/courses/add")
    public String addCourse(Model model ) {
        Course course = new Course();
        model.addAttribute("course", course);
        return "/views/courses/course-add";
    }
    
    
}
