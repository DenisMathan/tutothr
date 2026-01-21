package tutothr.booking;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tutothr.booking.timeslot.TimeSlot;
import tutothr.course.Course;
import tutothr.user.User;

/**
 * Buchung fuer ein Tutorium (TimeSlot).
 */
@Entity
@DiscriminatorValue("TIMESLOT")
public class TimeSlotBooking extends Booking {
	
	// === Felder ===
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	@ManyToOne
	@JoinColumn(name = "timeslot_id")
	private TimeSlot timeSlot;

	// === Konstruktoren ===
	
	public TimeSlotBooking() {
	}

	public TimeSlotBooking(User student, Course course, TimeSlot timeSlot, float price) {
		super(student, price);
		this.course = course;
		this.timeSlot = timeSlot;
	}

	// === Getter und Setter ===
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	// === Ueberschriebene Methoden ===
	
	@Override
	public String getBookingDescription() {
		return "Tutorium: " + course.getTitle() + " (" + timeSlot.getDate() + ", " + timeSlot.getStartTime() + "-"
				+ timeSlot.getEndTime() + ")";
	}
	
	@Override
	public User getTutor() {
		return timeSlot.getTutor();
	}
	
	@Override
	public TimeSlot cleanup() {
		if (timeSlot != null) {
			timeSlot.setAvailable(true);
			return timeSlot;
		}
		return null;
	}
}