package tutothr.course;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

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
        Course course = courseService.getCourseById(id);
        if (course != null) {
            model.addAttribute("course", course);
        } else {
            // Handle the case where the course is not found
            model.addAttribute("errorMessage", "Course not found");
            return "/error/404"; // Assuming you have an error view
        }
        model.addAttribute("fields", courseService.getFields());
        return "/views/courses/course";
    }

    @GetMapping({ "/tutor/courses/add" })
    public String getCreatePage(Model model, @PathVariable(required = false) Long id) {
        Course course = new Course();
        model.addAttribute("course", course);
        model.addAttribute("fields", courseService.getFields());
        return "/views/courses/course-edit";
    }

    @GetMapping({ "/tutor/courses/update/{id}" })
    public String getUpdatePage(Model model, @PathVariable(required = true) Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            model.addAttribute("course", course);
            model.addAttribute("fields", courseService.getFields());
        } else {
            // Handle the case where the course is not found
            model.addAttribute("errorMessage", "Course not found");
            return "/error/404"; // Assuming you have an error view
        }

        return "/views/courses/course-edit";
    }

    @PostMapping({ "/tutor/courses/save" })
    public String processCourseForm(@ModelAttribute Course course, BindingResult result) {
        if (result.hasErrors()) {
            return "/views/courses/course-edit";
        }
        // Creating a new course
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @PutMapping("/tutor/courses/save/{id}")
    public String putMethodName(@ModelAttribute Course course, BindingResult result, @RequestParam List<String> fields,
            @PathVariable(required = false) Long id) {
        if (result.hasErrors()) {
            return "/views/courses/course-edit";
        }
        Course existingCourse = courseService.getCourseById(course.getId());
        if (existingCourse != null) {
            Course updatedCourse = (Course) courseService.update(course, existingCourse);
            courseService.saveCourse(updatedCourse);
        } else {
            // Handle the case where the course is not found
            result.rejectValue("id", "error.course", "Course not found.");
            return "/views/courses/course-edit"; // Return to the form view with error message
        }
        return "redirect:/courses";
    }
}
