package tutothr.rating;

import jakarta.persistence.*;
import tutothr.common.BaseEntity;
import tutothr.common.models.Field;
import tutothr.course.Course;
import tutothr.user.User;

import java.util.List;

@Entity
public class Rating extends BaseEntity {

    private int stars;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User author;
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Rating() {
        super();
        init();
    }

    public void init() {
        formFields = List.of(
                new Field("author", "Autor", "long"),
                new Field("stars", "Sterne", "int"),
                new Field("comment", "Kommentar", "textarea")
        );
    }

    public void update(int stars, String comment) {
        setStars(stars);
        setComment(comment);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
