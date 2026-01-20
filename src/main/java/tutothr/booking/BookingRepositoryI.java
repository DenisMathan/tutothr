package tutothr.booking;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tutothr.booking.timeslot.TimeSlot;
import tutothr.common.MyBaseRepository;
import tutothr.course.Course;
import tutothr.user.User;

public interface BookingRepositoryI extends MyBaseRepository<Booking, Long> {
	List<Booking> findByStudent(User student);
	
	List<Booking> findByStudentAndStatus(User student, BookingStatus status);
	
	List<Booking> findByTimeSlotTutor(User tutor);
	
	List<Booking> findByTimeSlot(TimeSlot timeSlot);
	
	List<Booking> findByCourse(Course course);

	Page<Booking> findByStudent(User student, Pageable pageable);

	Page<Booking> findByTimeSlotTutor(User tutor, Pageable pageable);
	
	long countByStudent(User student);
	long countByTimeSlotTutor(User tutor);

	@Query("SELECT SUM(b.price) FROM Booking b WHERE b.timeSlot.tutor = :tutor AND b.status = 'COMPLETED'")
	Double calculateTotalRevenue(@Param("tutor") User tutor);

	@Query("SELECT SUM(b.price) FROM Booking b WHERE b.student = :student AND b.status = 'COMPLETED'")
	Double calculateTotalSpent(@Param("student") User student);

	@Query("SELECT SUM(b.price) FROM Booking b WHERE b.student = :student AND b.status = 'COMPLETED' AND b.createdAt >= :date")
	Double calculateSpentSince(@Param("student") User student, @Param("date") LocalDateTime date);

	@Query("SELECT b.course.title, SUM(b.price) as revenue " +
			"FROM Booking b " +
			"WHERE b.timeSlot.tutor = :tutor AND b.status = 'COMPLETED' " +
			"GROUP BY b.course.title " +
			"ORDER BY revenue DESC")
	List<Object[]> findBestPerformingCourse(@Param("tutor") User tutor, PageRequest pageable);

	boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
