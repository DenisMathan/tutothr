package tutothr.rating;

import jakarta.validation.constraints.*;
import tutothr.common.BaseDTO;
import tutothr.common.models.Field;
import tutothr.user.User;

import java.util.List;

public class RatingDTO extends BaseDTO {

    @NotNull
    @Min(1)
    @Max(5)
    private int stars;
    
    @Size(max = 1000)
    private String comment;
    private User author;
    private Long courseId;

    public void initFields() {
        formFields = List.of(
                new Field("author", "field.author", "long"),
                new Field("stars", "field.stars", "int"),
                new Field("comment", "field.comment", "textarea")
        );
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
