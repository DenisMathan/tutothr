package tutothr.user.Models;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import tutothr.common.models.TimeSlot;

@Entity
@Table(name="schedule")
@Inheritance(strategy=InheritanceType.JOINED)
public class Schedule {

    @Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	Long id;
    
    //TODO repeating
    private ArrayList<TimeSlot> vacations;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ArrayList<TimeSlot> getVacations() {
        return vacations;
    }
    public void setVacations(ArrayList<TimeSlot> vacations) {
        this.vacations = vacations;
    }
}
