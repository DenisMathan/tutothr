package tutothr.course;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import tutothr.auth.config.MyUserDetails;
import tutothr.category.CategoryDTO;
import tutothr.category.CategoryService;
import tutothr.hashtag.HashtagService;
import tutothr.rating.Rating;

@Controller
public class CourseController {
	private CourseService courseService;
	private CategoryService categoryService;
	private HashtagService hashtagService;

	public CourseController(CourseService courseService, CoursePermissionService coursePermissionService,
			CategoryService categoryService, HashtagService hashtagService) {
		this.courseService = courseService;
		this.categoryService = categoryService;
		this.hashtagService = hashtagService;
	}

//    @GetMapping("/courses")
//    public String getMethodName(Model model) {
//        List<CourseDTO> courses = courseService.getAllDTOs();
//        model.addAttribute("courses", courses);
//        return "/views/courses/courses";
//    }

	@GetMapping("/courses")
	public String getCourses(@ModelAttribute CourseSearchDTO searchDTO, Model model) {
		Page<CourseDTO> coursePage = courseService.search(searchDTO);

		model.addAttribute("courses", coursePage.getContent());
		model.addAttribute("searchDTO", searchDTO);
		model.addAttribute("currentPage", coursePage.getNumber());
		model.addAttribute("totalPages", coursePage.getTotalPages());
		model.addAttribute("totalItems", coursePage.getTotalElements());
		model.addAttribute("pageSize", coursePage.getSize());

		List<CategoryDTO> categories = categoryService.getAllDTOs();
		model.addAttribute("categories", categories);

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
		double avgRating = course.getRatings().stream().mapToInt(Rating::getStars).average().orElse(0.0);
		model.addAttribute("avgRating", avgRating);
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
		courseEntity.setOwnerId(((tutothr.auth.config.MyUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getId());
		// Creating a new course
		courseService.save(courseEntity);
		return "redirect:/courses";
	}

	@PutMapping("/tutor/courses/save/{id}")
	public String putMethodName(Model model, @ModelAttribute @Valid CourseDTO course, BindingResult result,
			@RequestParam List<String> fields, @PathVariable(required = true) Long id) {
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

	/**
	 * Hashtags zu einem Kurs hinzufuegen
	 */
	@PostMapping("/courses/{id}/hashtags")
	public String addHashtags(@PathVariable Long id, @RequestParam String hashtags,
			@AuthenticationPrincipal MyUserDetails userDetails) {
		hashtagService.addHashtagsToCourse(id, hashtags, userDetails.getDbUser());
		return "redirect:/courses/" + id;
	}

	/**
	 * Hashtag von einem Kurs entfernen
	 */
	@DeleteMapping("/courses/{id}/hashtags/{hashtagId}")
	public String removeHashtag(@PathVariable Long id, @PathVariable Long hashtagId,
			@AuthenticationPrincipal MyUserDetails userDetails) {
		boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		hashtagService.removeHashtagFromCourse(id, hashtagId, userDetails.getId(), isAdmin);
		return "redirect:/courses/" + id;
	}
}
