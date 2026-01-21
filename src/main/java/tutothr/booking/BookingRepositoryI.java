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

public interface BookingRepositoryI extends MyBaseRepository<Booking, Long> {
	
	// === Student-bezogene Abfragen ===
	
	List<Booking> findByStudent(User student);
	
	List<Booking> findByStudentAndStatus(User student, BookingStatus status);
	
	Page<Booking> findByStudent(User student, Pageable pageable);
	
	long countByStudent(User student);

	// === TimeSlot-Abfrage (nur fuer TimeSlotBooking) ===
	
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

	// === Revenue/Statistik-Abfragen ===
	
	@Query("SELECT COALESCE(SUM(b.price), 0) FROM Booking b WHERE " +
	       "b.status = 'COMPLETED' AND (" +
	       "(TYPE(b) = TimeSlotBooking AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor) OR " +
	       "(TYPE(b) = CourseBooking AND TREAT(b AS CourseBooking).course.owner = :tutor) OR " +
	       "(TYPE(b) = ChapterBooking AND TREAT(b AS ChapterBooking).chapter.course.owner = :tutor))")
	Double calculateTotalRevenue(@Param("tutor") User tutor);

	@Query("SELECT SUM(b.price) FROM Booking b WHERE b.student = :student AND b.status = 'COMPLETED'")
	Double calculateTotalSpent(@Param("student") User student);

	@Query("SELECT SUM(b.price) FROM Booking b WHERE b.student = :student AND b.status = 'COMPLETED' AND b.createdAt >= :date")
	Double calculateSpentSince(@Param("student") User student, @Param("date") LocalDateTime date);

	@Query("SELECT TREAT(b AS TimeSlotBooking).course.title, SUM(b.price) as revenue " +
	       "FROM Booking b " +
	       "WHERE TYPE(b) = TimeSlotBooking " +
	       "AND TREAT(b AS TimeSlotBooking).timeSlot.tutor = :tutor " +
	       "AND b.status = 'COMPLETED' " +
	       "GROUP BY TREAT(b AS TimeSlotBooking).course.title " +
	       "ORDER BY revenue DESC")
	List<Object[]> findBestPerformingCourse(@Param("tutor") User tutor, PageRequest pageable);

	// === Pruefung ob Student einen Kurs gebucht hat ===
	
	@Query("SELECT COUNT(b) > 0 FROM CourseBooking b WHERE b.student.id = :studentId AND b.course.id = :courseId AND b.status = 'CONFIRMED'")
	boolean existsByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
	
	@Query("SELECT COUNT(b) > 0 FROM ChapterBooking b WHERE b.student.id = :studentId AND b.chapter.id = :chapterId AND b.status = 'CONFIRMED'")
	boolean existsByStudentIdAndChapterId(@Param("studentId") Long studentId, @Param("chapterId") Long chapterId);

	@Query("SELECT b.chapter.id FROM ChapterBooking b WHERE b.student.id = :studentId AND b.chapter.course.id = :courseId AND b.status = 'CONFIRMED'")
	Set<Long> findPurchasedChapterIdsByUserAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}