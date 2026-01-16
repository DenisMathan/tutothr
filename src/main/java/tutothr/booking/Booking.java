package tutothr.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import tutothr.booking.invoice.Invoice;
import tutothr.booking.timeslot.TimeSlot;
import tutothr.common.BaseEntity;
import tutothr.course.Course;
import tutothr.user.User;

@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private User student;
	
	@ManyToOne
	@JoinColumn(name = "timeslot_id", nullable = false)
	private TimeSlot timeSlot;
	
	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status = BookingStatus.PENDING;
	
	private float price;
	
	@OneToOne(mappedBy = "booking")
	private Invoice invoice;
	
	public Booking() {
		// Default-Konstruktor (fuer JPA)
	}
	
	public Booking(User student, TimeSlot timeSlot, Course course, float price) {
		this.student = student;
		this.timeSlot = timeSlot;
		this.course = course;
		// status wird automatisch PENDING durch Feld-Initialisierung
		this.price = price;
	}
	
	// Getter und Setter
	
	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public Invoice getInvoice() {
	    return invoice;
	}

	public void setInvoice(Invoice invoice) {
	    this.invoice = invoice;
	}
}
