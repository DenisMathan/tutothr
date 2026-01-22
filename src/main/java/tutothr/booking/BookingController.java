package tutothr.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tutothr.auth.config.AppPrincipal;
import tutothr.auth.config.MyUserDetails;
import tutothr.booking.timeslot.TimeSlotDTO;
import tutothr.booking.timeslot.TimeSlotService;
import tutothr.course.Course;
import tutothr.course.CourseDTO;
import tutothr.course.CourseService;
import tutothr.user.User;

@Controller
public class BookingController {
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 2;

	private final BookingService bookingService;
	private final TimeSlotService timeSlotService;
	private final CourseService courseService;

	@Autowired
	private CalendarService calendarService;

	public BookingController(BookingService bookingService, TimeSlotService timeSlotService,
			CourseService courseService) {
		this.bookingService = bookingService;
		this.timeSlotService = timeSlotService;
		this.courseService = courseService;
	}

	// ===== STUDENT: Einzelnes Chapter kaufen =====

	@PostMapping("/chapter/{chapterId}/buy")
	public String buyChapter(@PathVariable Long chapterId,
	        @AuthenticationPrincipal /*MyUserDetails*/ AppPrincipal userDetails, RedirectAttributes redirectAttributes) {
	    BookingDTO booking = bookingService.createChapterBooking(userDetails.getDbUser(), chapterId);

	    if (booking == null) {
	        redirectAttributes.addFlashAttribute("error", "Kauf fehlgeschlagen.");
	        return "redirect:/courses";
	    }
	    
	    return "redirect:/booking/" + booking.getId() + "/pay";
	}

	// ===== STUDENT: Ganzen Kurs (Inhalt) kaufen =====

	@PostMapping("/course/{courseId}/buy")
	public String buyCourse(@PathVariable Long courseId,
	        @AuthenticationPrincipal /*MyUserDetails*/ AppPrincipal userDetails, RedirectAttributes redirectAttributes) {
	    BookingDTO booking = bookingService.createCourseBooking(userDetails.getDbUser(), courseId);

	    if (booking == null) {
	        redirectAttributes.addFlashAttribute("error", "Kauf fehlgeschlagen.");
	        return "redirect:/course/" + courseId;
	    }
	    
	    return "redirect:/booking/" + booking.getId() + "/pay";
	}
	
	// ===== STUDENT: Buchung erstellen =====

	@GetMapping("/course/{courseId}/book")
	public String showBookingForm(@PathVariable Long courseId, Model model) {
		// Entity fuer Logik (ownerId holen)
		Course courseEntity = courseService.findById(courseId);

		User tutor = courseEntity.getOwner();
		List<TimeSlotDTO> availableSlots = timeSlotService.findAvailableByTutor(tutor);

		// DTO fuer View
		CourseDTO courseDTO = courseService.findDTOById(courseId);

		model.addAttribute("course", courseDTO);
		model.addAttribute("timeSlots", availableSlots);
		model.addAttribute("hourlyRate", tutor.getHourlyRate());
		return "views/booking/book";
	}

	@PostMapping("/course/{courseId}/book")
	public String createBooking(@PathVariable Long courseId, @RequestParam Long timeSlotId,
			@AuthenticationPrincipal /*MyUserDetails*/ AppPrincipal userDetails, RedirectAttributes redirectAttributes) {
		BookingDTO booking = bookingService.createTimeSlotBooking(userDetails.getDbUser(), courseId, timeSlotId);

		if (booking == null) {
			redirectAttributes.addFlashAttribute("error",
					"Buchung fehlgeschlagen. Der Termin ist nicht mehr verfügbar.");
			return "redirect:/course/" + courseId + "/book";
		}

		// Direkt zur Zahlung weiterleiten
		return "redirect:/booking/" + booking.getId() + "/pay";
	}

	// ===== STUDENT: Meine Buchungen =====

	@GetMapping("/my-bookings")
	public String myBookings(@AuthenticationPrincipal /*MyUserDetails*/ AppPrincipal userDetails,
			org.springframework.security.core.Authentication authentication,
			@RequestParam(defaultValue = "0") int page, Model model) {
		Page<BookingDTO> bookingPage = bookingService.findByStudentPaged(userDetails.getDbUser(),
				PageRequest.of(page, DEFAULT_SIZE, Sort.by(Sort.Direction.DESC, "createdAt")));

		model.addAttribute("bookings", bookingPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", bookingPage.getTotalPages());
		model.addAttribute("totalItems", bookingPage.getTotalElements());
		
		// Prüfen ob User mit Google eingeloggt ist (für Kalender-Sync)
		boolean isGoogleUser = authentication instanceof OAuth2AuthenticationToken;
		model.addAttribute("isGoogleUser", isGoogleUser);

		return "views/booking/my-bookings";
	}

	// ===== Termin zu Google Calendar hinzufügen =====
	@GetMapping("/booking/{id}/add-to-calendar")
	public String addToCalendar(@PathVariable Long id, 
			org.springframework.security.core.Authentication authentication,
			RedirectAttributes redirectAttributes) {
		try {
			// Prüfen ob der User mit Google (OAuth2) eingeloggt ist
			if (!(authentication instanceof OAuth2AuthenticationToken)) {
				redirectAttributes.addFlashAttribute("error", 
					"Google Calendar Sync ist nur verfügbar, wenn du dich mit Google angemeldet hast.");
				return "redirect:/my-bookings";
			}
			
			OAuth2AuthenticationToken auth = (OAuth2AuthenticationToken) authentication;

			BookingDTO booking = bookingService.findById(id);
			TimeSlotDTO timeslot = timeSlotService.findById(booking.getTimeSlotId());

			calendarService.addEventToGoogleCalendar(auth, booking.getBookingDescription(), "Gebucht über Uni-App",
					timeslot.getDate(), timeslot.getStartTime(), timeslot.getEndTime());

			redirectAttributes.addFlashAttribute("success", "Termin erfolgreich zu Google Calendar hinzugefügt!");
		} catch (Exception e) {
			e.printStackTrace(); // Fürs Debugging in der Konsole wichtig!
			redirectAttributes.addFlashAttribute("error", "Fehler beim Kalender-Sync: " + e.getMessage());
		}
		return "redirect:/my-bookings"; // Zurück zur Liste
	}

	// ===== TUTOR: Buchungen fuer meine TimeSlots =====

	@GetMapping("/tutor/bookings")
	public String tutorBookings(@AuthenticationPrincipal /*MyUserDetails*/ AppPrincipal userDetails,
			@RequestParam(defaultValue = "0") int page, Model model) {
		Page<BookingDTO> bookingPage = bookingService.findByTutorPaged(userDetails.getDbUser(),
				PageRequest.of(page, DEFAULT_SIZE, Sort.by(Sort.Direction.DESC, "created_at")));

		model.addAttribute("bookings", bookingPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", bookingPage.getTotalPages());
		model.addAttribute("totalItems", bookingPage.getTotalElements());

		return "views/booking/tutor-bookings";
	}
	
	// ===== STUDENT: Buchung stornieren =====

	@GetMapping("/booking/{id}/cancel")
	public String cancelBooking(@PathVariable Long id,
	        @AuthenticationPrincipal /*MyUserDetails*/ AppPrincipal userDetails,
	        RedirectAttributes redirectAttributes) {
	    BookingDTO booking = bookingService.findById(id);
	    
	    // Sicherheitscheck: Nur eigene PENDING-Buchungen stornieren
	    if (booking != null 
	            && booking.getStudentId().equals(userDetails.getDbUser().getId())
	            && booking.getStatus() == BookingStatus.PENDING) {
	        bookingService.cancelAndCleanup(id);
	        redirectAttributes.addFlashAttribute("success", "Buchung storniert.");
	    } else {
	        redirectAttributes.addFlashAttribute("error", "Stornierung nicht möglich.");
	    }
	    return "redirect:/my-bookings";
	}
}
