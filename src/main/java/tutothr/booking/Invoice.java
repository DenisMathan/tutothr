package tutothr.booking;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import tutothr.common.BaseEntity;

@Entity
@Table(name = "invoice")
public class Invoice extends BaseEntity {
	private String invoiceNumber; // z. B. 2026-0001

	@OneToOne
	@JoinColumn(name = "booking_id", nullable = false)
	private Booking booking;

	// Snapshot-Felder (Werte zum Zeitpunkt der Zahlung)
	private String studentName;
	private String tutorName;
	private String courseName;
	private float price;
	private LocalDateTime paidAt;

	public Invoice() {
		// Default-Konstruktor (fuer JPA)
	}

	public Invoice(String invoiceNumber, Booking booking, String studentName, String tutorName, String courseName,
			float price, LocalDateTime paidAt) {
		this.invoiceNumber = invoiceNumber;
		this.booking = booking;
		this.studentName = studentName;
		this.tutorName = tutorName;
		this.courseName = courseName;
		this.price = price;
		this.paidAt = paidAt;
	}

	// Getter und Setter

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}
}
