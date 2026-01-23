package tutothr.course;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import tutothr.booking.BookingService;
import tutothr.booking.ContentAccessService;
import tutothr.category.Category;
import tutothr.category.CategoryDTO;
import tutothr.category.CategoryService;
import tutothr.chapter.ChapterDTO;
import tutothr.chapter.ChapterViewModel;
import tutothr.hashtag.HashtagService;
import tutothr.rating.Rating;
import tutothr.user.UserService;

@Controller
public class CourseController {
	
	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

	private final CourseService courseService;
	private final CategoryService categoryService;
	private final HashtagService hashtagService;
	private final BookingService bookingService;
	private final ContentAccessService contentAccessService;
	private final UserService userService;
    // Injecting the permission service is not strictly needed for @PreAuthorize string, 
    // but good to have if we used it programmatically. 
    // Here we rely on SpEL finding the bean by name "coursePermissionService".

	public CourseController(CourseService courseService,
			CategoryService categoryService, HashtagService hashtagService, 
			BookingService bookingService, ContentAccessService contentAccessService, UserService userService) {
		this.courseService = courseService;
		this.categoryService = categoryService;
		this.hashtagService = hashtagService;
		this.bookingService = bookingService;
		this.contentAccessService = contentAccessService;
		this.userService = userService;
	}

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

		return "views/courses/courses";
	}

	@GetMapping("/tutor/courses")
	@PreAuthorize("hasRole('TUTOR')")
	public String getMyCourses(Model model, @AuthenticationPrincipal AppPrincipal userDetails) {
		Long userId = userDetails.getId();
		List<CourseDTO> courses = courseService.findByOwner(userService.getUserById(userId));
		model.addAttribute("courses", courses);
		return "views/courses/my-courses";
	}

	@GetMapping("/tutor/courses/add")
    @PreAuthorize("hasRole('TUTOR')")
	public String getCreatePage(Model model) {
		CourseDTO course = new CourseDTO();
		course.updateCategoryField(categoryService.getAllDTOs());
		model.addAttribute("course", course);
		return "views/courses/course-edit";
	}

	@GetMapping("/courses/{id}")
	public String getCourseDetails(Model model, @PathVariable Long id, @AuthenticationPrincipal AppPrincipal userDetails) {
		Course course = courseService.findById(id);
		if (course == null) {
			model.addAttribute("errorMessage", "Course not found");
			return "/error/404";
		}
		
		CourseDTO courseDTO = courseService.mapToDTO(course);
		double avgRating = course.getRatings().stream().mapToInt(Rating::getStars).average().orElse(0.0);
		model.addAttribute("avgRating", avgRating);
		model.addAttribute("course", courseDTO);

		Long userId = userDetails != null ? userDetails.getId() : null;
		
		boolean hasBooked = false;
		if (userId != null) {
			hasBooked = bookingService.hasUserBookedCourse(userId, id);
		}
		model.addAttribute("hasBooked", hasBooked);
		model.addAttribute("hourlyRate", course.getOwner().getHourlyRate());

		// Create Chapter View Models
		List<ChapterViewModel> chapterViews = createChapterViewModels(courseDTO, userId, id);
		model.addAttribute("chapterViews", chapterViews);

		// Berechne welche Hashtags der User entfernen darf
		Set<Long> removableHashtagIds = Set.of();
		if (userId != null) {
			boolean isAdmin = userDetails.getAuthorities().stream()
					.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
			removableHashtagIds = hashtagService.getRemovableHashtagIds(id, userId, isAdmin);
		}
		model.addAttribute("removableHashtagIds", removableHashtagIds);

		return "views/courses/course";
	}

	@GetMapping("/tutor/courses/update/{id}")
    @PreAuthorize("@coursePermissionService.isTutorAndOwnerOrAdmin(#id)")
	public String getUpdatePage(Model model, @PathVariable Long id) {
		CourseDTO course = courseService.findDTOById(id);

		if (course == null) {
			model.addAttribute("errorMessage", "Course not found");
			return "/error/404";
		}
		

		course.updateCategoryField(categoryService.getAllDTOs());
		model.addAttribute("course", course);
		
		return "views/courses/course-edit";
	}

	@PostMapping("/tutor/courses/save")
    @PreAuthorize("hasRole('TUTOR')")
	public String processCourseForm(Model model, @ModelAttribute @Valid CourseDTO course, BindingResult result, 
			@AuthenticationPrincipal AppPrincipal userDetails) {
		if (result.hasErrors()) {
			course.updateCategoryField(categoryService.getAllDTOs());
			course = courseService.handleValidationErrors(course, result.getFieldErrors());
			model.addAttribute("course", course);
			return "views/courses/course-edit";
		}
		
		Course courseEntity = courseService.mapToEntity(course);
		List<Category> categories = new ArrayList<>();
		if (course.getCategoryIds() != null) {
			categories = categoryService.findAllEntitiesByIds(course.getCategoryIds());
		}
		courseEntity.setCategories(categories);
		courseEntity.setOwner(userDetails.getDbUser());

		courseService.save(courseEntity);
		return "redirect:/courses";
	}

	@PutMapping("/tutor/courses/save/{id}")
    @PreAuthorize("@coursePermissionService.isTutorAndOwnerOrAdmin(#id)")
	public String updateCourse(Model model, @ModelAttribute @Valid CourseDTO courseDTO, BindingResult result,
			@PathVariable Long id) {
		logger.debug("Updating course {}, title: '{}'", id, courseDTO.getTitle());
		
		if (result.hasErrors()) {
			logger.debug("Validation errors: {}", result.getAllErrors());
			courseDTO.updateCategoryField(categoryService.getAllDTOs());
			courseDTO = courseService.handleValidationErrors(courseDTO, result.getFieldErrors());
			model.addAttribute("course", courseDTO);
			return "views/courses/course-edit";
		}

		Course existingCourse = courseService.findById(id);
		if (existingCourse == null) {
			return "redirect:/courses"; 
		}

		// Update fields from DTO
		existingCourse.setTitle(courseDTO.getTitle());
		existingCourse.setDescription(courseDTO.getDescription());
		existingCourse.setPrice(courseDTO.getPrice());

		List<Category> categories = new ArrayList<>();
		if (courseDTO.getCategoryIds() != null) {
			categories = categoryService.findAllEntitiesByIds(courseDTO.getCategoryIds());
		}
		existingCourse.setCategories(categories);

		courseService.save(existingCourse);
		return "redirect:/courses";
	}

	@DeleteMapping("/tutor/courses/delete/{id}")
    @PreAuthorize("@coursePermissionService.isTutorAndOwnerOrAdmin(#id)")
	public String deleteCourse(@PathVariable Long id) {
		courseService.deleteById(id);
		return "redirect:/courses";
	}

	/**
	 * Hashtags zu einem Kurs hinzufuegen
	 */
	@PostMapping("/courses/{id}/hashtags")
    @PreAuthorize("isAuthenticated()")
	public String addHashtags(@PathVariable Long id, @RequestParam String hashtags,
			@AuthenticationPrincipal AppPrincipal userDetails) {
		hashtagService.addHashtagsToCourse(id, hashtags, userDetails.getDbUser());
		return "redirect:/courses/" + id;
	}

	/**
	 * Hashtag von einem Kurs entfernen
	 */
	@DeleteMapping("/courses/{id}/hashtags/{hashtagId}")
    @PreAuthorize("isAuthenticated()")
	public String removeHashtag(@PathVariable Long id, @PathVariable Long hashtagId,
			@AuthenticationPrincipal AppPrincipal userDetails) {
		boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		hashtagService.removeHashtagFromCourse(id, hashtagId, userDetails.getId(), isAdmin);
		return "redirect:/courses/" + id;
	}

	private List<ChapterViewModel> createChapterViewModels(CourseDTO courseDTO, Long userId, Long courseId) {
		boolean isOwner = courseDTO.getIsOwner();
		Set<Long> accessibleChapterIds = null;
		boolean hasCourseAccess = false;
		
		if (userId != null) {
			accessibleChapterIds = contentAccessService.getAccessibleChapterIds(userId, courseId);
			hasCourseAccess = (accessibleChapterIds == null); // null = whole course purchased
		}

		List<ChapterViewModel> chapterViews = new ArrayList<>();
		for (ChapterDTO chapter : courseDTO.getChapters()) {
			boolean isAccessible = isOwner 
					|| !chapter.isPaywalled() 
					|| hasCourseAccess
					|| (accessibleChapterIds != null && accessibleChapterIds.contains(chapter.getId()));
			
			boolean isPurchasable = !isOwner && chapter.isPaywalled() && !isAccessible;
			chapterViews.add(new ChapterViewModel(chapter, isAccessible, isPurchasable));
		}
		return chapterViews;
	}
}
