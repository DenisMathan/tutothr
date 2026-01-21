package tutothr.booking;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import tutothr.booking.invoice.Invoice;
import tutothr.booking.timeslot.TimeSlot;
import tutothr.common.BaseEntity;
import tutothr.user.User;

@Entity
@Table(name = "booking")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "booking_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Booking extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private User student;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status = BookingStatus.PENDING;
	
	private float price;
	
	@OneToOne(mappedBy = "booking")
	private Invoice invoice;
	
	public Booking() {
		// Default-Konstruktor (fuer JPA)
	}
	
	public Booking(User student, float price) {
        this.student = student;
        this.price = price;
    }
	
	// Abstrakte Methode fuer Anzeige
    public abstract String getBookingDescription();
    
    public abstract User getTutor();
    
    public abstract TimeSlot cleanup();
	
	// Getter und Setter
	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
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
