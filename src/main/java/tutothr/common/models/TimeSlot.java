package tutothr.common.models;

import java.security.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import tutothr.common.utils.enums.StatusEnum;



@Entity
@Table(name="timeslot")
public class TimeSlot {
    @Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	Long id;

    private Timestamp startTime;
    private Timestamp endTime;
    private StatusEnum state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public StatusEnum getState() {
        return state;
    }

    public void setState(StatusEnum state) {
        this.state = state;
    }

}
