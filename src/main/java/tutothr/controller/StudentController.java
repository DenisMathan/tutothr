package tutothr.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tutothr.model.Student;
import tutothr.service.CourseServiceI;
import tutothr.service.StudentServiceI;

@Controller
public class StudentController {
    private StudentServiceI studentService;
    private CourseServiceI courseService;

    public StudentController(StudentServiceI studentService, CourseServiceI courseService) {
        super();
        System.out.println("controller is here");
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping(value = {"/list" })
    public String showUserList(Model model, @RequestParam(required = false) String keyword, @RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "5") int size) {
        System.out.println("where is list?");
        try {
            List<Student> students = new ArrayList<Student>();

            // the first page is 1 for the user, 0 for the database.
            Pageable paging = PageRequest.of(page - 1, size);
            Page<Student> pageStudents;
            // getting the page from the database….
            pageStudents = studentService.getAllStudents(keyword, paging);

            model.addAttribute("keyword", keyword);

            students = pageStudents.getContent();
            model.addAttribute("students", students);
            // here are the variables for the paginator in the student-all view
            model.addAttribute("entitytype", "student");
            model.addAttribute("currentPage", pageStudents.getNumber() + 1);
            model.addAttribute("totalItems", pageStudents.getTotalElements());
            model.addAttribute("totalPages", pageStudents.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "students/student-all";
    }
}
