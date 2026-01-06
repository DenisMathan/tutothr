package tutothr.user.Models;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import tutothr.common.models.TimeSlotOld;

@Entity
@Table(name="schedule")
@Inheritance(strategy=InheritanceType.JOINED)
public class Schedule {

    @Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	Long id;
    
    //TODO repeating
    private ArrayList<TimeSlotOld> vacations;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ArrayList<TimeSlotOld> getVacations() {
        return vacations;
    }
    public void setVacations(ArrayList<TimeSlotOld> vacations) {
        this.vacations = vacations;
    }
}
