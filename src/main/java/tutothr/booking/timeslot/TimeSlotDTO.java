package tutothr.booking.timeslot;

import java.time.LocalDate;
import java.time.LocalTime;

import tutothr.common.BaseDTO;

public class TimeSlotDTO extends BaseDTO {
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private boolean available = true;
	private Long tutorId;
	
	@Override
    public void initFields() {
		// Formular manuell erstellt
    }
	
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

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }
}
