package tutothr.category;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import tutothr.common.BaseEntity;
import tutothr.course.Course;

@Entity
public class Category extends BaseEntity {
    String title;
    String description;

    public Category() {
        super();
        init();
    }
    
    public void init() {
        formFields = List.of(
            new tutothr.common.models.Field("title", "Titel", "text"),
            new tutothr.common.models.Field("description", "Beschreibung", "textarea")
        );
    }

    @ManyToMany(mappedBy = "categories")
    private List<Course> courses;
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

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}