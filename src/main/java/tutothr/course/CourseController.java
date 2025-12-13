package tutothr.course;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tutothr.common.models.Field;

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

    @GetMapping("/courses/{id}")
    public String addCourse(Model model, @PathVariable(required = true) Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
        } else {
            // Handle the case where the course is not found
            model.addAttribute("errorMessage", "Course not found");
            return "/error/404"; // Assuming you have an error view
        }
        model.addAttribute("fields", List.of(
                new Field("title", "Titel", "text"),
                new Field("description", "Beschreibung", "textarea"),
                new Field("price", "Preis", "number")
        // usw.
        ));
        return "/views/courses/course-edit";
    }

    @GetMapping({ "/tutor/courses/add" })
    public String getCreatePage(Model model, @PathVariable(required = false) Long id) {
        Course course = new Course();
        model.addAttribute("course", course);
        model.addAttribute("fields", List.of(
                new Field("title", "Titel", "text"),
                new Field("description", "Beschreibung", "textarea"),
                new Field("price", "Preis", "number")
        // usw.
        ));
        return "/views/courses/course-edit";
    }

    @GetMapping({ "/tutor/courses/update/{id}" })
    public String getUpdatePage(Model model, @PathVariable(required = true) Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
        } else {
            // Handle the case where the course is not found
            model.addAttribute("errorMessage", "Course not found");
            return "/error/404"; // Assuming you have an error view
        }

        return "/views/courses/course-edit";
    }
    @PostMapping({ "/tutor/courses/save/{id}", "/tutor/courses/save"})
    public String processCourseForm(@ModelAttribute Course course, @RequestParam List<String> fields, BindingResult result, @PathVariable(required = false) Long id) {
        if (result.hasErrors()) {
            return "/views/courses/course-edit";
        }
        if(id == null) {
            // Creating a new course
            courseService.saveCourse(course);
            return "redirect:/courses";
        }
        Optional<Course> _course = courseService.getCourseById(id);
        if(_course.isPresent()) {
            Course existingCourse = _course.get();
            for (String fieldName : fields) {
                try {
                    java.lang.reflect.Field field = Course.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object newValue = field.get(course);
                    field.set(existingCourse, newValue);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Optional: Logging oder Fehlerbehandlung
                    e.printStackTrace();
                }
            }
            courseService.saveCourse(existingCourse);
        } else {
            // Handle the case where the course is not found
            result.rejectValue("id", "error.course", "Course not found.");
            return "/views/courses/course-edit"; // Return to the form view with error message
        }
        // courseService.saveCourse(course);
       return "redirect:/courses"; 
    }

}
