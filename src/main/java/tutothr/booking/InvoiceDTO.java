package tutothr.booking;

import java.time.LocalDateTime;

import tutothr.common.BaseDTO;

public class InvoiceDTO extends BaseDTO {
	private String invoiceNumber;
	private Long bookingId;
	
	// Snapshot-Felder
	private String studentName;
	private String tutorName;
	private String courseName;
	private float price;
	private LocalDateTime paidAt;
	
	@Override
	public void initFields() {
		// Kein Formular noetig
	}

	// Getter und Setter
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
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
