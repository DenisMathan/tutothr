package tutothr.chapter;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tutothr.common.BaseEntity;
import tutothr.course.Course;
import tutothr.common.models.Field;

@Entity
public class Chapter extends BaseEntity {

    private String title;
    private String description;
    private int position;
    private boolean paywalled;

    public Chapter() {
       super();
       init();
    }

    public void init() {
       formFields = List.of(
            new Field("title", "Titel", "text"),
            new Field("description", "Beschreibung", "textarea")
        ); 
    }


    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // Getter/Setter
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
    public boolean isPaywalled() {
        return paywalled;
    }
    public void setPaywalled(boolean paywalled) {
        this.paywalled = paywalled;
    }
    public Long getOwnerId() {
        return this.course.getOwnerId();
    }
}