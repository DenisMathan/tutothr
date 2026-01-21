package tutothr.booking;

import tutothr.common.BaseDTO;

/**
 * DTO fuer Buchungen. Enthaelt Daten fuer alle drei Buchungstypen
 * (Kurs, Kapitel, Tutorium) sowie Anzeige-Informationen fuer Views.
 */
public class BookingDTO extends BaseDTO {
	
	// === Felder ===
	
	private Long studentId;
	private Long timeSlotId;
	private Long courseId;
	private Long chapterId;
	private BookingStatus status;
	private float price;
	private Long invoiceId;
	
	private String bookingType;
	
	// Fuer die Anzeige in Views
	private String studentName;
	private String studentEmail;
	private String bookingDescription;
		
	// Fuer Tutor-Benachrichtigungen
	private String tutorName;
	private String tutorEmail;
	
	// === Methoden ===
	
	@Override
	public void initFields() {
		// Formular manuell erstellt
	}

	// === Getter und Setter ===
	
	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getTimeSlotId() {
		return timeSlotId;
	}

	public void setTimeSlotId(Long timeSlotId) {
		this.timeSlotId = timeSlotId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getChapterId() {
		return chapterId;
	}

	public void setChapterId(Long chapterId) {
		this.chapterId = chapterId;
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

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}
	
	public String getBookingDescription() {
		return bookingDescription;
	}

	public void setBookingDescription(String bookingDescription) {
		this.bookingDescription = bookingDescription;
	}
	
	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}
	
	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getTutorEmail() {
		return tutorEmail;
	}

	public void setTutorEmail(String tutorEmail) {
		this.tutorEmail = tutorEmail;
	}
}