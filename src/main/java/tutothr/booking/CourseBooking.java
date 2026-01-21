package tutothr.booking;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tutothr.booking.timeslot.TimeSlot;
import tutothr.course.Course;
import tutothr.user.User;

/**
 * Buchung fuer einen kompletten Kurs (alle Kapitel).
 */
@Entity
@DiscriminatorValue("COURSE")
public class CourseBooking extends Booking {
	
	// === Felder ===
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	// === Konstruktoren ===
	
	public CourseBooking() {
	}

	public CourseBooking(User student, Course course, float price) {
		super(student, price);
		this.course = course;
	}

	// === Getter und Setter ===
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	// === Ueberschriebene Methoden ===
	
	@Override
	public String getBookingDescription() {
		return "Kurs: " + course.getTitle();
	}
	
	@Override
	public User getTutor() {
		return course.getOwner();
	}
	
	@Override
	public TimeSlot cleanup() {
		return null;
	}
}