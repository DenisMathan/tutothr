package tutothr.booking;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tutothr.common.BaseEntity;
import tutothr.course.Course;
import tutothr.user.User;

@Entity
@Table(name = "timeslot")
public class TimeSlot extends BaseEntity {
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private boolean available = true;
	
	@ManyToOne
	@JoinColumn(name = "tutor_id", nullable = false)
	private User tutor;

	// Getter und Setter
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public User getTutor() {
		return tutor;
	}

	public void setTutor(User tutor) {
		this.tutor = tutor;
	}
}
