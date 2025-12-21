package tutothr.course;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService, CoursePermissionService coursePermissionService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String getMethodName(Model model) {
        List<CourseDTO> courses = courseService.getAllDTOs();
        model.addAttribute("courses", courses);
        return "/views/courses/courses";
    }

    @GetMapping("/courses/{id}")
    public String addCourse(Model model, @PathVariable(required = true) Long id) {
        Course course = courseService.findById(id);
        if (course == null) {
            model.addAttribute("errorMessage", "Course not found");
            return "/error/404"; 
        }
        CourseDTO courseDTO = courseService.mapToDTO(course);
        model.addAttribute("course", courseDTO);
        return "/views/courses/course";
    }

    @GetMapping({ "/tutor/courses/add" })
    public String getCreatePage(Model model, @PathVariable(required = false) Long id) {
        CourseDTO course = new CourseDTO();
        model.addAttribute("course", course);
        return "/views/courses/course-edit";
    }

    @GetMapping({ "/tutor/courses/update/{id}" })
    public String getUpdatePage(Model model, @PathVariable(required = true) Long id) {
        CourseDTO course = courseService.findDTOById(id);

        if (course != null) {
            if (!course.getIsOwner()) {
                // Handle unauthorized access
                model.addAttribute("errorMessage", "You are not authorized to edit this course.");
                return "/error/403"; // Assuming you have a 403 error view
            }
            model.addAttribute("course", course);
        } else {
            // Handle the case where the course is not found
            model.addAttribute("errorMessage", "Course not found");
            return "/error/404"; // Assuming you have an error view
        }

        return "/views/courses/course-edit";
    }

    @PostMapping({ "/tutor/courses/save" })
    public String processCourseForm(Model model, @ModelAttribute @Valid CourseDTO course, BindingResult result) {
        if (result.hasErrors()) {
            course = courseService.handleValidationErrors(course, result.getFieldErrors());
            model.addAttribute("course", course);
            return "/views/courses/course-edit";
        }
        Course courseEntity = courseService.mapToEntity(course);
        courseEntity.setOwnerId(((tutothr.auth.config.MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        // Creating a new course
        courseService.save(courseEntity);
        return "redirect:/courses";
    }

    @PutMapping("/tutor/courses/save/{id}")
    public String putMethodName(Model model, @ModelAttribute @Valid CourseDTO course, BindingResult result, @RequestParam List<String> fields, @PathVariable(required = true) Long id) {
        if (result.hasErrors()) {
            course = courseService.handleValidationErrors(course, result.getFieldErrors());
            model.addAttribute("course", course);
            return "/views/courses/course-edit";
        }
        courseService.update(course);
        return "redirect:/courses";
    }

    @DeleteMapping("/tutor/courses/delete/{id}")
    public String deleteCourse(@PathVariable(required = true) Long id) {
        courseService.deleteById(id);
        return "redirect:/courses";
    }
}
