package tutothr.repository;

import java.util.List;

import tutothr.model.Course;

public interface CourseRepositoryI extends MyBaseRepository<Course, Long> {

	
	List<Course> findByDescriptionContainingIgnoreCase (String description);
}
