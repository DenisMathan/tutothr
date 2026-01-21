package tutothr.booking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tutothr.auth.config.AppPrincipal;
import tutothr.user.User;
import tutothr.user.UserService;

/**
 * REST-API fuer Buchungen.
 * Ermoeglicht CRUD-Operationen auf Buchungen fuer authentifizierte Benutzer.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingApi {

	// === Felder ===
	
	private final BookingService bookingService;
	private final UserService userService;

	// === Konstruktor ===
	
	public BookingApi(BookingService bookingService, UserService userService) {
		this.bookingService = bookingService;
		this.userService = userService;
	}

	// === READ ===

	@GetMapping
	public ResponseEntity<List<BookingDTO>> getMyBookings(@AuthenticationPrincipal AppPrincipal principal) {
		User user = userService.findById(principal.getId());
		List<BookingDTO> bookings = bookingService.findByStudent(user);
		return ResponseEntity.ok(bookings);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id,
			@AuthenticationPrincipal AppPrincipal principal) {
		BookingDTO booking = bookingService.findById(id);
		
		if (booking == null) {
			return ResponseEntity.notFound().build();
		}
		
		// Zugriffspruefung: Nur eigene Buchungen
		if (!booking.getStudentId().equals(principal.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return ResponseEntity.ok(booking);
	}

	// === CREATE ===

	@PostMapping("/course/{courseId}")
	public ResponseEntity<BookingDTO> bookCourse(@PathVariable Long courseId,
			@AuthenticationPrincipal AppPrincipal principal) {
		User student = userService.findById(principal.getId());
		BookingDTO booking = bookingService.createCourseBooking(student, courseId);
		
		if (booking == null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(booking);
	}

	@PostMapping("/chapter/{chapterId}")
	public ResponseEntity<BookingDTO> bookChapter(@PathVariable Long chapterId,
			@AuthenticationPrincipal AppPrincipal principal) {
		User student = userService.findById(principal.getId());
		BookingDTO booking = bookingService.createChapterBooking(student, chapterId);
		
		if (booking == null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(booking);
	}

	@PostMapping("/timeslot")
	public ResponseEntity<BookingDTO> bookTimeSlot(@RequestParam Long courseId,
			@RequestParam Long timeSlotId,
			@AuthenticationPrincipal AppPrincipal principal) {
		User student = userService.findById(principal.getId());
		BookingDTO booking = bookingService.createTimeSlotBooking(student, courseId, timeSlotId);
		
		if (booking == null) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(booking);
	}

	// === DELETE ===

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelBooking(@PathVariable Long id,
			@AuthenticationPrincipal AppPrincipal principal) {
		BookingDTO booking = bookingService.findById(id);
		
		if (booking == null) {
			return ResponseEntity.notFound().build();
		}
		
		// Zugriffspruefung: Nur eigene Buchungen stornieren
		if (!booking.getStudentId().equals(principal.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		// Nur PENDING Buchungen koennen storniert werden
		if (booking.getStatus() != BookingStatus.PENDING) {
			return ResponseEntity.badRequest().build();
		}
		
		bookingService.cancelAndCleanup(id);
		return ResponseEntity.noContent().build();
	}
}