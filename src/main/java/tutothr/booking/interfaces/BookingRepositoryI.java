package tutothr.booking.interfaces;

import java.util.List;

import tutothr.booking.Booking;
import tutothr.booking.BookingStatus;
import tutothr.booking.TimeSlot;
import tutothr.common.MyBaseRepository;
import tutothr.course.Course;
import tutothr.user.User;

public interface BookingRepositoryI extends MyBaseRepository<Booking, Long> {
	List<Booking> findByStudent(User student);
	
	List<Booking> findByStudentAndStatus(User student, BookingStatus status);
	
	List<Booking> findByTimeSlotTutor(User tutor);
	
	List<Booking> findByTimeSlot(TimeSlot timeSlot);
	
	List<Booking> findByCourse(Course course);
}
