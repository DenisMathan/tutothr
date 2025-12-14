package tutothr.chapter;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tutothr.common.BaseEntity;
import tutothr.course.Course;

@Entity
public class Chapter extends BaseEntity {

    private String title;
    private String content;
    private int position;
    private boolean paywalled;

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
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
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
}