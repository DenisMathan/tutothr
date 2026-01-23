package tutothr.hashtag;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tutothr.course.Course;
import tutothr.user.User;

public interface CourseHashtagLinkRepositoryI extends JpaRepository<CourseHashtagLink, Long> {

    List<CourseHashtagLink> findByCourse(Course course);

    List<CourseHashtagLink> findByCourseId(Long courseId);

    Optional<CourseHashtagLink> findByCourseAndHashtag(Course course, Hashtag hashtag);

    Optional<CourseHashtagLink> findByCourseIdAndHashtagId(Long courseId, Long hashtagId);

    boolean existsByCourseAndHashtag(Course course, Hashtag hashtag);

    void deleteByCourseAndHashtag(Course course, Hashtag hashtag);

    List<CourseHashtagLink> findByAddedBy(User user);

    void deleteByAddedBy(User user);
}
