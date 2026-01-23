package tutothr.hashtag;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import tutothr.common.BaseEntity;
import tutothr.course.Course;
import tutothr.user.User;

/**
 * Verknuepfung zwischen Course und Hashtag.
 * Speichert zusaetzlich, wer das Tag zu diesem Kurs hinzugefuegt hat.
 */
@Entity
@Table(
    name = "course_hashtag_link",
    uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "hashtag_id"})
)
public class CourseHashtagLink extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;

    @ManyToOne
    @JoinColumn(name = "added_by_id")
    private User addedBy;

    // === Konstruktoren ===

    public CourseHashtagLink() {
    }

    public CourseHashtagLink(Course course, Hashtag hashtag, User addedBy) {
        this.course = course;
        this.hashtag = hashtag;
        this.addedBy = addedBy;
    }

    // === Getter und Setter ===

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Hashtag getHashtag() {
        return hashtag;
    }

    public void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }
}
