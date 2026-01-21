package tutothr.course;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import tutothr.auth.config.AppPrincipal;
import tutothr.auth.config.MyUserDetails;
import tutothr.booking.BookingService;
import tutothr.booking.ContentAccessService;
import tutothr.category.Category;
import tutothr.category.CategoryDTO;
import tutothr.category.CategoryService;
import tutothr.chapter.ChapterDTO;
import tutothr.chapter.ChapterViewModel;
import tutothr.hashtag.HashtagService;
import tutothr.rating.Rating;

@Controller
public class CourseController {
	private CourseService courseService;
	private CategoryService categoryService;
	private HashtagService hashtagService;
	@Autowired
	private BookingService bookingService;
	private ContentAccessService contentAccessService;

	public CourseController(CourseService courseService, CoursePermissionService coursePermissionService,
			CategoryService categoryService, HashtagService hashtagService, ContentAccessService contentAccessService) {
		this.courseService = courseService;
		this.categoryService = categoryService;
		this.hashtagService = hashtagService;
		this.contentAccessService = contentAccessService;
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

	    Long userId = getCurrentUserId();
	    boolean hasBooked = bookingService.hasUserBookedCourse(userId, id);
	    model.addAttribute("hasBooked", hasBooked);
	    model.addAttribute("hourlyRate", course.getOwner().getHourlyRate());
	    
	    // ChapterViewModels erstellen
	    boolean isOwner = courseDTO.getIsOwner();
	    Set<Long> accessibleChapterIds = contentAccessService.getAccessibleChapterIds(userId, id);
	    boolean hasCourseAccess = (accessibleChapterIds == null); // null = ganzer Kurs gekauft
	    
	    List<ChapterViewModel> chapterViews = new ArrayList<>();
	    for (ChapterDTO chapter : courseDTO.getChapters()) {
	        boolean accessible = isOwner || !chapter.isPaywalled() || hasCourseAccess 
	                || (accessibleChapterIds != null && accessibleChapterIds.contains(chapter.getId()));
	        boolean purchasable = !isOwner && chapter.isPaywalled() && !accessible;
	        chapterViews.add(new ChapterViewModel(chapter, accessible, purchasable));
	    }
	    model.addAttribute("chapterViews", chapterViews);
	    
	    return "/views/courses/course";
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
			course.updateCategoryField(categoryService.getAllDTOs());
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
			course.updateCategoryField(categoryService.getAllDTOs());
			course = courseService.handleValidationErrors(course, result.getFieldErrors());
			model.addAttribute("course", course);
			return "/views/courses/course-edit";
		}
		Course courseEntity = courseService.mapToEntity(course);
		List<Category> categories = new java.util.ArrayList<>();
		if (course.getCategoryIds() != null) {
			categories = categoryService.findAllEntitiesByIds(course.getCategoryIds());
		}
		courseEntity.setCategories(categories);

		courseEntity.setOwner(((tutothr.auth.config.MyUserDetails) SecurityContextHolder.getContext()
		        .getAuthentication().getPrincipal()).getDbUser());
		// Creating a new course
		courseService.save(courseEntity);
		return "redirect:/courses";
	}

	@PutMapping("/tutor/courses/save/{id}")
	public String putMethodName(Model model, @ModelAttribute @Valid CourseDTO course, BindingResult result,
			@PathVariable(required = true) Long id) {
		System.out.println("DEBUG: Updating course " + id + ", title: '" + course.getTitle() + "'");
		if (result.hasErrors()) {
			System.out.println("DEBUG: Validation errors: " + result.getAllErrors());
			course.updateCategoryField(categoryService.getAllDTOs());
			course = courseService.handleValidationErrors(course, result.getFieldErrors());
			model.addAttribute("course", course);
			return "/views/courses/course-edit";
		}

		Course existingCourse = courseService.findById(id);
		if (existingCourse == null) {
			return "redirect:/courses"; // handle error better ideally
		}

		// Update fields from DTO
		existingCourse.setTitle(course.getTitle());
		existingCourse.setDescription(course.getDescription());
		existingCourse.setPrice(course.getPrice());

		List<Category> categories = new java.util.ArrayList<>();
		if (course.getCategoryIds() != null) {
			categories = categoryService.findAllEntitiesByIds(course.getCategoryIds());
		}
		existingCourse.setCategories(categories);

		courseService.save(existingCourse);
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

	private Long getCurrentUserId() {
		return ((AppPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
	}
}
