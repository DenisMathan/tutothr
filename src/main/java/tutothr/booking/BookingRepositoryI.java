package tutothr.booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tutothr.booking.timeslot.TimeSlot;
import tutothr.common.MyBaseRepository;
import tutothr.user.User;

/**
 * Repository fuer Buchungen. Enthaelt Abfragen fuer alle drei Buchungstypen
 * (Kurs, Kapitel, Tutorium) sowie Statistik- und Zugriffspruefungen.
 */
public interface BookingRepositoryI extends MyBaseRepository<Booking, Long> {
	
	// === Student-bezogene Abfragen ===
	
	List<Booking> findByStudent(User student);
	
	List<Booking> findByStudentAndStatus(User student, BookingStatus status);
	
	Page<Booking> findByStudent(User student, Pageable pageable);
	
	long countByStudent(User student);

	// === TimeSlot-Abfrage ===
	
	@Query("SELECT b FROM TimeSlotBooking b WHERE b.timeSlot = :timeSlot")
	List<Booking> findByTimeSlot(@Param("timeSlot") TimeSlot timeSlot);

	// === Tutor-bezogene Abfragen (alle Booking-Typen) ===
	
	@Query("SELECT b FROM Booking b WHERE " +
	       "(TYPE(b) = TimeSlotBooking AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor) OR " +
	       "(TYPE(b) = CourseBooking AND TREAT(b AS CourseBooking).course.owner = :tutor) OR " +
	       "(TYPE(b) = ChapterBooking AND TREAT(b AS ChapterBooking).chapter.course.owner = :tutor)")
	List<Booking> findByTutor(@Param("tutor") User tutor);

	@Query("SELECT b FROM Booking b WHERE " +
	       "(TYPE(b) = TimeSlotBooking AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor) OR " +
	       "(TYPE(b) = CourseBooking AND TREAT(b AS CourseBooking).course.owner = :tutor) OR " +
	       "(TYPE(b) = ChapterBooking AND TREAT(b AS ChapterBooking).chapter.course.owner = :tutor)")
	Page<Booking> findByTutor(@Param("tutor") User tutor, Pageable pageable);
	
	@Query("SELECT COUNT(b) FROM Booking b WHERE " +
	       "(TYPE(b) = TimeSlotBooking AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor) OR " +
	       "(TYPE(b) = CourseBooking AND TREAT(b AS CourseBooking).course.owner = :tutor) OR " +
	       "(TYPE(b) = ChapterBooking AND TREAT(b AS ChapterBooking).chapter.course.owner = :tutor)")
	long countByTutor(@Param("tutor") User tutor);
	
	@Query(value = "SELECT b.* FROM booking b " +
		       "LEFT JOIN timeslot ts ON ts.id = b.timeslot_id " +
		       "LEFT JOIN course c ON c.id = b.course_id " +
		       "LEFT JOIN chapter ch ON ch.id = b.chapter_id " +
		       "LEFT JOIN course c2 ON c2.id = ch.course_id " +
		       "WHERE (b.booking_type = 'TIMESLOT' AND ts.tutor_id = :tutorId) " +
		       "OR (b.booking_type = 'COURSE' AND c.owner_id = :tutorId) " +
		       "OR (b.booking_type = 'CHAPTER' AND c2.owner_id = :tutorId)",
		       countQuery = "SELECT COUNT(b.id) FROM booking b " +
		       "LEFT JOIN timeslot ts ON ts.id = b.timeslot_id " +
		       "LEFT JOIN course c ON c.id = b.course_id " +
		       "LEFT JOIN chapter ch ON ch.id = b.chapter_id " +
		       "LEFT JOIN course c2 ON c2.id = ch.course_id " +
		       "WHERE (b.booking_type = 'TIMESLOT' AND ts.tutor_id = :tutorId) " +
		       "OR (b.booking_type = 'COURSE' AND c.owner_id = :tutorId) " +
		       "OR (b.booking_type = 'CHAPTER' AND c2.owner_id = :tutorId)",
		       nativeQuery = true)
	Page<Booking> findByTutorIdNative(@Param("tutorId") Long tutorId, Pageable pageable);

	// === Revenue/Statistik-Abfragen ===
	
	@Query("SELECT COALESCE(SUM(b.price), 0) FROM Booking b WHERE " +
	       "b.status = 'CONFIRMED' AND (" +
	       "(TYPE(b) = TimeSlotBooking AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor) OR " +
	       "(TYPE(b) = CourseBooking AND TREAT(b AS CourseBooking).course.owner = :tutor) OR " +
	       "(TYPE(b) = ChapterBooking AND TREAT(b AS ChapterBooking).chapter.course.owner = :tutor))")
	Double calculateTotalRevenue(@Param("tutor") User tutor);

	@Query("SELECT SUM(b.price) FROM Booking b WHERE b.student = :student AND b.status = 'CONFIRMED'")
	Double calculateTotalSpent(@Param("student") User student);

	@Query("SELECT SUM(b.price) FROM Booking b WHERE b.student = :student AND b.status = 'CONFIRMED' AND b.createdAt >= :date")
	Double calculateSpentSince(@Param("student") User student, @Param("date") LocalDateTime date);

	@Query("SELECT TREAT(b AS TimeSlotBooking).course.title, SUM(b.price) as revenue " +
	       "FROM Booking b " +
	       "WHERE TYPE(b) = TimeSlotBooking " +
	       "AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor " +
	       "AND b.status = 'CONFIRMED' " +
	       "GROUP BY TREAT(b AS TimeSlotBooking).course.title " +
	       "ORDER BY revenue DESC")
	List<Object[]> findBestPerformingCourse(@Param("tutor") User tutor, PageRequest pageable);

	@Query("SELECT COALESCE(SUM(b.price), 0) FROM CourseBooking b WHERE b.status = 'CONFIRMED' AND b.course.owner.id = :tutorId")
	Double calculateCourseRevenue(@Param("tutorId") Long tutorId);

	@Query("SELECT COALESCE(SUM(b.price), 0) FROM ChapterBooking b WHERE b.status = 'CONFIRMED' AND b.chapter.course.owner.id = :tutorId")
	Double calculateChapterRevenue(@Param("tutorId") Long tutorId);

	@Query("SELECT COALESCE(SUM(b.price), 0) FROM TimeSlotBooking b WHERE b.status = 'CONFIRMED' AND b.timeSlot.tutor.id = :tutorId")
	Double calculateTimeSlotRevenue(@Param("tutorId") Long tutorId);

	@Query("""
	    SELECT TREAT(b AS TimeSlotBooking).course.title, COUNT(b.id)
	    FROM Booking b
	    WHERE TYPE(b) = TimeSlotBooking
	    AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor
	    GROUP BY TREAT(b AS TimeSlotBooking).course.title
	    ORDER BY COUNT(b.id) DESC
	    """)
	List<Object[]> findMostBookedCourse(@Param("tutor") User tutor, PageRequest pageable);

	// === Zugriffspruefungen ===
	
	@Query("SELECT COUNT(b) > 0 FROM CourseBooking b WHERE b.student.id = :studentId AND b.course.id = :courseId AND b.status = 'CONFIRMED'")
	boolean existsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
	
	@Query("SELECT COUNT(b) > 0 FROM ChapterBooking b WHERE b.student.id = :studentId AND b.chapter.id = :chapterId AND b.status = 'CONFIRMED'")
	boolean existsByStudentIdAndChapterId(@Param("studentId") Long studentId, @Param("chapterId") Long chapterId);

	@Query("SELECT b.chapter.id FROM ChapterBooking b WHERE b.student.id = :studentId AND b.chapter.course.id = :courseId AND b.status = 'CONFIRMED'")
	Set<Long> findPurchasedChapterIdsByUserAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}