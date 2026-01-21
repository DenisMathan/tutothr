package tutothr.course.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tutothr.category.Category;
import tutothr.common.MyBaseRepository;
import tutothr.course.Course;

public interface CourseRepositoryI extends MyBaseRepository<Course, Long> {

    // Zählt Kurse eines Tutors
    long countByOwner_Id(Long ownerId);

    // Berechnet das Durchschnitts-Rating aller Kurse eines Tutors (Profi-Query!)
    @Query("SELECT AVG(r.stars) FROM Course c JOIN c.ratings r WHERE c.owner.id = :tutorId")
    Double getAverageRatingByTutor(@Param("tutorId") Long tutorId);
    List<Course> findByCategoriesContaining(Category category);
    
}
