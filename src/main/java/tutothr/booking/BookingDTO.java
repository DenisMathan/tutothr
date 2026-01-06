package tutothr.booking;

import tutothr.common.BaseDTO;

public class BookingDTO extends BaseDTO {
	private Long studentId;
	private Long timeSlotId;
	private Long courseId;
	private BookingStatus status;
	private float price;
	private Long invoiceId;
	
	// Fuer die Anzeige in Views
	private String studentName;
	private String courseName;
	private String timeSlotDisplay;
	
	@Override
	public void initFields() {
		// Formular manuell erstellt
	}

	// Getter und Setter
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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTimeSlotDisplay() {
		return timeSlotDisplay;
	}

	public void setTimeSlotDisplay(String timeSlotDisplay) {
		this.timeSlotDisplay = timeSlotDisplay;
	}
	
	public Long getInvoiceId() {
	    return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
	    this.invoiceId = invoiceId;
	}
}
