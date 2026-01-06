package tutothr.booking;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import tutothr.booking.interfaces.BookingMapperI;
import tutothr.booking.interfaces.BookingRepositoryI;
import tutothr.booking.interfaces.TimeSlotRepositoryI;
import tutothr.course.Course;
import tutothr.course.interfaces.CourseRepositoryI;
import tutothr.user.User;

@Service
public class BookingService {
	private final BookingRepositoryI bookingRepository;
	private final BookingMapperI mapper;
	private final TimeSlotRepositoryI timeSlotRepository;
	private final CourseRepositoryI courseRepository;

	public BookingService(BookingRepositoryI bookingRepository, BookingMapperI mapper,
			TimeSlotRepositoryI timeSlotRepository, CourseRepositoryI courseRepository) {
		this.bookingRepository = bookingRepository;
		this.mapper = mapper;
		this.timeSlotRepository = timeSlotRepository;
		this.courseRepository = courseRepository;
	}

	public BookingDTO findById(Long id) {
		return bookingRepository.findById(id)
				.map(this::toDTO)
				.orElse(null);
	}

	public List<BookingDTO> findByStudent(User student) {
		return bookingRepository.findByStudent(student)
				.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	public List<BookingDTO> findByTutor(User tutor) {
		return bookingRepository.findByTimeSlotTutor(tutor)
				.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	public List<BookingDTO> findByCourse(Course course) {
		return bookingRepository.findByCourse(course)
				.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	public void deleteById(Long id) {
		bookingRepository.deleteById(id);
	}
	
	public void updateStatus(Long id, BookingStatus status) {
		Booking booking = bookingRepository.findById(id).orElse(null);
		if (booking != null) {
			booking.setStatus(status);
			bookingRepository.save(booking);
		}
	}

	public BookingDTO createBooking(User student, Long timeSlotId, Long courseId) {
		// TimeSlot laden
		TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId).orElse(null);
		if (timeSlot == null || !timeSlot.isAvailable()) {
			return null; // Spaeter Exception werfen
		}
		
		// Course laden
		Course course = courseRepository.findById(courseId).orElse(null);
		if (course == null) {
			return null; // Spaeter Exception werfen
		}
		
		// Booking erstellen
		Booking booking = new Booking(student, timeSlot, course, course.getPrice());
		
		// TimeSlot als nicht mehr verfuegbar markieren
		timeSlot.setAvailable(false);
		timeSlotRepository.save(timeSlot);
		
		// Booking speichern und zurueckgeben
		Booking saved = bookingRepository.save(booking);
		return toDTO(saved);
	}

	private BookingDTO toDTO(Booking booking) {
		BookingDTO dto = mapper.toDTO(booking);

		// timeSlotDisplay manuell befuellen
		TimeSlot ts = booking.getTimeSlot();
		String display = ts.getDate() + ", " + ts.getStartTime() + "-" + ts.getEndTime();
		dto.setTimeSlotDisplay(display);

		return dto;
	}
}
