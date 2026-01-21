package tutothr.booking;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tutothr.booking.timeslot.TimeSlot;
import tutothr.booking.timeslot.TimeSlotRepositoryI;
import tutothr.chapter.Chapter;
import tutothr.chapter.interfaces.ChapterRepositoryI;
import tutothr.course.Course;
import tutothr.course.interfaces.CourseRepositoryI;
import tutothr.user.User;

@Service
public class BookingService {
	private final BookingRepositoryI bookingRepository;
	private final TimeSlotRepositoryI timeSlotRepository;
	private final CourseRepositoryI courseRepository;
	private final ChapterRepositoryI chapterRepository;
	private final List<BookingDTOMapper> mappers;

	public BookingService(BookingRepositoryI bookingRepository, TimeSlotRepositoryI timeSlotRepository,
			CourseRepositoryI courseRepository, ChapterRepositoryI chapterRepository,
			List<BookingDTOMapper> mappers) {
		this.bookingRepository = bookingRepository;
		this.timeSlotRepository = timeSlotRepository;
		this.courseRepository = courseRepository;
		this.chapterRepository = chapterRepository;
        this.mappers = mappers;
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

	public Page<BookingDTO> findByStudentPaged(User student, Pageable pageable) {
		return bookingRepository.findByStudent(student, pageable)
				.map(this::toDTO);
	}

//	public List<BookingDTO> findByTutor(User tutor) {
//		return bookingRepository.findByTutor(tutor)
//				.stream()
//				.map(this::toDTO)
//				.collect(Collectors.toList());
//	}
	public List<BookingDTO> findByTutor(User tutor) {
	    return bookingRepository.findByTutorIdNative(tutor.getId(), Pageable.unpaged())
	            .stream()
	            .map(this::toDTO)
	            .collect(Collectors.toList());
	}
	
//	public Page<BookingDTO> findByTutorPaged(User tutor, Pageable pageable) {
//		return bookingRepository.findByTutor(tutor, pageable)
//				.map(this::toDTO);
//	}
	public Page<BookingDTO> findByTutorPaged(User tutor, Pageable pageable) {
	    return bookingRepository.findByTutorIdNative(tutor.getId(), pageable)
	            .map(this::toDTO);
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

	// === Methoden fuer verschiedene Buchungstypen ===

    public BookingDTO createChapterBooking(User student, Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
        if (chapter == null || chapter.getPrice() == null) {
            return null;
        }
        
        // Bereits gekauft? (Kapitel oder ganzer Kurs)
        if (bookingRepository.existsByStudentIdAndChapterId(student.getId(), chapterId)
                || bookingRepository.existsByStudentIdAndCourseId(student.getId(), chapter.getCourse().getId())) {
            return null;
        }
        
        ChapterBooking booking = new ChapterBooking(student, chapter, chapter.getPrice());
        Booking saved = bookingRepository.save(booking);
        return toDTO(saved);
    }

    public BookingDTO createCourseBooking(User student, Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return null;
        }
        
        // Bereits gekauft?
        if (bookingRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            return null;
        }
        
        CourseBooking booking = new CourseBooking(student, course, course.getPrice());
        Booking saved = bookingRepository.save(booking);
        return toDTO(saved);
    }

    public BookingDTO createTimeSlotBooking(User student, Long courseId, Long timeSlotId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId).orElse(null);
        
        if (course == null || timeSlot == null || !timeSlot.isAvailable()) {
            return null;
        }
        
        // Preis vom Tutor (hourlyRate)
        User tutor = timeSlot.getTutor();
        float price = tutor.getHourlyRate() != null ? tutor.getHourlyRate() : 0f;
        
        TimeSlotBooking booking = new TimeSlotBooking(student, course, timeSlot, price);
        
        // TimeSlot als nicht mehr verfuegbar markieren
        timeSlot.setAvailable(false);
        timeSlotRepository.save(timeSlot);
        
        Booking saved = bookingRepository.save(booking);
        return toDTO(saved);
    }
	
	public void cancelAndCleanup(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId).orElse(null);
		if (booking == null) {
			return;
		}

		TimeSlot timeSlotToSave = booking.cleanup();
        if (timeSlotToSave != null) {
            timeSlotRepository.save(timeSlotToSave);
        }
        
        bookingRepository.delete(booking);
	}
	
	// === Hilfsmethoden ===

    private BookingDTO toDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        
        // Gemeinsame Felder
        dto.setId(booking.getId());
        dto.setStudentId(booking.getStudent().getId());
        dto.setStudentName(booking.getStudent().getUsername());
        dto.setStudentEmail(booking.getStudent().getEmail());
        dto.setStatus(booking.getStatus());
        dto.setPrice(booking.getPrice());
        dto.setBookingDescription(booking.getBookingDescription());
        
        // Tutor-Felder (gemeinsam fuer alle Typen)
        User tutor = booking.getTutor();
        dto.setTutorName(tutor.getUsername());
        dto.setTutorEmail(tutor.getEmail());
        
        if (booking.getInvoice() != null) {
            dto.setInvoiceId(booking.getInvoice().getId());
        }
        
        // Typ-spezifische Felder via Mapper
        for (BookingDTOMapper mapper : mappers) {
            if (mapper.supports(booking)) {
                mapper.fillDTO(booking, dto);
                break;
            }
        }
        
        return dto;
    }
	
	public boolean hasUserBookedCourse(Long userId, Long courseId) {
		return bookingRepository.existsByStudentIdAndCourseId(userId, courseId);
	}
}
