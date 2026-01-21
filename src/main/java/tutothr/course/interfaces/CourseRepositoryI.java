package tutothr.course.interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tutothr.category.Category;
import tutothr.common.MyBaseRepository;
import tutothr.course.Course;
import tutothr.user.User;

public interface CourseRepositoryI extends MyBaseRepository<Course, Long> {

    // Zählt Kurse eines Tutors
    long countByOwner_Id(Long ownerId);

    // Findet alle Kurse eines Tutors
    List<Course> findByOwner(User owner);

    // Berechnet das Durchschnitts-Rating aller Kurse eines Tutors (Profi-Query!)
    @Query("SELECT AVG(r.stars) FROM Course c JOIN c.ratings r WHERE c.owner.id = :tutorId")
    Double getAverageRatingByTutor(@Param("tutorId") Long tutorId);
    List<Course> findByCategoriesContaining(Category category);

    //bestbewertester Kurs eines Tutors
    @Query("""
    SELECT c.title, AVG(r.stars), COUNT(r.id)
    FROM Course c
    LEFT JOIN c.ratings r
    WHERE c.owner.id = :tutorId
    GROUP BY c.id, c.title
    HAVING COUNT(r.id) > 0
    ORDER BY AVG(r.stars) DESC, COUNT(r.id) DESC
    """)
    List<Object[]> findBestRatedCourseByTutor(@Param("tutorId") Long tutorId, Pageable pageable);
}
