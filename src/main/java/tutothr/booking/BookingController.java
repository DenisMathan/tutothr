package tutothr.booking;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tutothr.auth.config.MyUserDetails;
import tutothr.booking.timeslot.TimeSlotDTO;
import tutothr.booking.timeslot.TimeSlotService;
import tutothr.course.Course;
import tutothr.course.CourseDTO;
import tutothr.course.CourseService;
import tutothr.user.User;
import tutothr.user.UserService;

@Controller
public class BookingController {
	private final BookingService bookingService;
	private final TimeSlotService timeSlotService;
	private final CourseService courseService;
	private final UserService userService;

	public BookingController(BookingService bookingService, TimeSlotService timeSlotService,
			CourseService courseService, UserService userService) {
		this.bookingService = bookingService;
		this.timeSlotService = timeSlotService;
		this.courseService = courseService;
		this.userService = userService;
	}

	// ===== STUDENT: Buchung erstellen =====

	@GetMapping("/course/{courseId}/book")
	public String showBookingForm(@PathVariable Long courseId, Model model) {
		// Entity fuer Logik (ownerId holen)
		Course courseEntity = courseService.findById(courseId);

		User tutor = userService.getUserById(courseEntity.getOwnerId());
		List<TimeSlotDTO> availableSlots = timeSlotService.findAvailableByTutor(tutor);

		// DTO fuer View
		CourseDTO courseDTO = courseService.findDTOById(courseId);

		model.addAttribute("course", courseDTO);
		model.addAttribute("timeSlots", availableSlots);
		return "views/booking/book";
	}

	@PostMapping("/course/{courseId}/book")
	public String createBooking(@PathVariable Long courseId, @RequestParam Long timeSlotId,
			@AuthenticationPrincipal MyUserDetails userDetails, RedirectAttributes redirectAttributes) {
		BookingDTO booking = bookingService.createBooking(userDetails.getDbUser(), timeSlotId, courseId);

		if (booking == null) {
			redirectAttributes.addFlashAttribute("error",
					"Buchung fehlgeschlagen. Der Termin ist nicht mehr verfügbar.");
			return "redirect:/course/" + courseId + "/book";
		}
		return "redirect:/my-bookings";
	}

	// ===== STUDENT: Meine Buchungen =====

	@GetMapping("/my-bookings")
	public String myBookings(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
		List<BookingDTO> bookings = bookingService.findByStudent(userDetails.getDbUser());
		model.addAttribute("bookings", bookings);
		return "views/booking/my-bookings";
	}

	// ===== TUTOR: Buchungen fuer meine TimeSlots =====

	@GetMapping("/tutor/bookings")
	public String tutorBookings(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
		List<BookingDTO> bookings = bookingService.findByTutor(userDetails.getDbUser());
		model.addAttribute("bookings", bookings);
		return "views/booking/tutor-bookings";
	}
}
