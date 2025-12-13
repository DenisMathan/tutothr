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